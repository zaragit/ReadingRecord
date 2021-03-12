# 스프링 들어가며
* 스프링이란?  
  + 자바 엔터프라이즈 어플리케이션을 개발하는데 사용되는 프레임 워크이다.   

## 애플리케이션의 기본 틀 - 스프링 컨테이너
스프링은 스프링 컨테이너(혹은 어플리케이션 컨텍스트)라는 런타임 엔진을 제공

- 스프링 컨테이너의 역할
    - 스프링 컨테이너는 설정정보를 읽어 오브젝트를 관리한다.  


## 공통 프로그래밍 모델 - IOC/DI, 서비스 추상화, AOP
스프링 프레임워크는 3가지 프로그래밍 모델( 어플리케이션 코드가 어떻게 작성되어야하는지 가이드)을 제공  

# 1. 오브젝트와 의존관계
- 분리와 확장  
소스코드 변경 가능성에 대해 작업을 최소화 하고 변경된 부분이 다른 곳에 문제를 일으키지 않을 수 있도록 하는  
- 관심사의 분리 (프로그래밍 기초 개념 중  하나로 Separation of Concerns)  
관심이 같은것 끼리 하나의 객체 안으로 하고 관심이 다른 객체 들은 따로 분리하여 서로 영향을 주지 않도록 분리


- 예제 코드 관심사 분리

1. db 연결과 관련된 관심

    → db connection 메서드를 추가하여 db 연결 관심사를 분리

2. sql 실행 및 바인딩
3. 리소스 반환

관심사를 분리하므로서 코드의 변동사항이 있을때 관심사만 변경하면되므로 다른 코드에 영향을 주지않는다.  

- 리팩토링
1. 메소드 추출  
독립된 메소드로 분리  
```java
 package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {

    public void create() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE USERS (ID VARCHAR(10) PRIMARY KEY, NAME VARCHAR (20) NOT NULL, PASSWORD VARCHAR(10) NOT NULL)");

        ps.executeUpdate();

        ps.close();
        c.close();

    }


    public void add(User user) throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();

    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }


    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection("jdbc:h2:~/test:AUTO_SERVER=true","sa","sa");
        return c;
    }
}
```  
2. 상속을 통한 확장  
상하위 클래스로 분리
```java
package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public abstract class UserDao {

    public void create() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE USERS (ID VARCHAR(10) PRIMARY KEY, NAME VARCHAR (20) NOT NULL, PASSWORD VARCHAR(10) NOT NULL)");

        ps.executeUpdate();

        ps.close();
        c.close();

    }


    public void add(User user) throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();

    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }


    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
```  
- 템플릿 메소드 패턴  
슈퍼클래스에 로직의 흐름을 만들고 기능의 일부를 추상 메서드나 오바라이딩이 가능한 protected 메소드 등으로 만든뒤 서브클래스에서 구현해서 사용    

- 팩토리 메소드 패턴  
슈퍼 클래스에서 추상메서드로 어떤 기능인지 정의한 뒤, 하위 클래스에서 구체적인 정의를 하는것

3. 상속관계가 아닌 완전히 독립적인 클래스로 분리  
db 커넥션 클래스가 연결할 디비 정보를 알고있어야하며 UserDao가 디비연결 클래스에 종속적이므로 db 커넥션을 가져오는 방법에대해 확장이 힘들어졌다.

```java
package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection("jdbc:h2:~/test:AUTO_SERVER=true", "sa", "sa");
        return c;
    }
}
```  
```java
package springbook.user.dao;

import springbook.user.domain.User;
import java.sql.*;

public abstract class UserDao {

    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDao(){
        simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void create() throws SQLException, ClassNotFoundException {
        Connection c = simpleConnectionMaker.getConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE USERS (ID VARCHAR(10) PRIMARY KEY, NAME VARCHAR (20) NOT NULL, PASSWORD VARCHAR(10) NOT NULL)");

        ps.executeUpdate();

        ps.close();
        c.close();

    }


    public void add(User user) throws SQLException, ClassNotFoundException {
        Connection c = simpleConnectionMaker.getConnection();
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();

    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.getConnection();
        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}
``` 

4. 인터페이스 도입

두개의 클래스가 긴밀하게 연결되지 않도록 중간에 추상적인 연결고리 추가

- 추상화

어떤 것들의 공통적인 성격을 뽑아내 이를 따로 분리하는 작업 → 인터페이스(어떤 일을 하겠다는 기능만 정의해놓은것, 구현은 없음, 구현은 인터페이스를 구현한 클래스들이 할일) 이용

인터페이스를 사용하는 UserDao가 어떤 클래스인지 상관없이 

- 높은 응집도

변화가 일어날 때 해당 모듈에서 변하는 부분이 큰것

- 낮은 결합도

느슨한 연결관계 유지하는데 필요한 최소한의 간접적인 형태로 제공, 나머지는 서로 독립적이고 알필요도 없게 만드는것

결합도 : 하나의 오브젝트가 변경이 일어날 때에 관계를 맺고있는 다른 오브젝트에게 변화를 요구하는 정도


- 전략 패턴 

자신의 기능 맥락 (context, 자신의 기능을 수행하는데 필요한 기능중 변경가능한 기능)에서, 필요에 따라 변경이 필요한 알고리즘을 인터페이스를 통해 외부로 분리시키고,

이를 구현한 구체적인 알고리즘 클래스를 필요에따라 바꿔서 사용할 수 있게하는 디자인 패턴


- 프레임워크(제어의 역전 개념 적용)

어플리케이션 코드가 프레임워크에 의해 사용됨

프레임워크가 흐름을 주도하는 중에 개발자가만든 어플리케이션 코드를 사용하도록 하는 방식


- 라이브러리

라이브러리를 사용하는 애플리케이션 코드의 흐름에 직접 제어

동작 중 필요한 기능이 있을때 능동적으로 라이브러리 사용


## 1.5 스프링의 IOC

- bean

    스프링이 제어권을 가지고 직접 생성, 관계설정, 사용 제어하는 오브젝트

    자바빈과 비슷한 오브젝트 단위의 애플리케이션 컴포넌트

- bean factory

    스프링에서 빈의 생성과 관계설정 같은 제어를 담당하는 ioc 오브젝트를 beanfactory라 한다.

    → 예제에서 DaoFactory 클래스가 하는 역할

    → bean factory보다는 더 확장한 application context(IOC 방식을 따라 만들어진 일종의 bean factory) 주로 사용



 




