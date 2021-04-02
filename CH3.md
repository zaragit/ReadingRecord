템플릿 : 바뀌는 성질이 다른 코드 중에서 다른 코드 중에서 변경이 거의 일어나지 않으며 일정한 패턴으로 유지되는 특성을 가진 부분을 자유롭게 변경되는 성질을 가진 부분으로부터 독립시켜서 효과적으로 활용할 수 있도록 하는 방법 

JDBC 코드는 try catch finally 문으로 인해 복잡해진다. 이를 더 좋은 구조로 한번 만들어 보자. 

1. 템플릿 메소드 패턴으로 수정하기
- 템플릿 메소드 패턴 : 상속을 통해 기능을 확장해서 사용하는 부분이다. 변하지 않는 부분은 슈퍼클래스에 두고, 변하는 부분은 추상 메소드로 정의한 뒤 서브 클래스에서 오버라이드한다.  
- Dao 로직마다 상속을 통해 새로운 클래스를 만들어야한다. / 확장 구조가 고정된다.

``` java
// 추상클래스 extends
public class UserDaoDeleteAll extends UserDao {
    @Override
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("delete from users");
    }
}

```

2. 전략 패턴으로 수정하기
- 전략 패턴 : 오브젝트를 둘로 분리하고 클래스 레벨에서는 인터페이스를 통해서만 의존하도록 만든다.
- Dao 메소드마다 새로운 구현 클래스를 만들어야한다는 단점이 있다. (AddStatement, DeletAllStatement ...)
- 클래스 파일이 많아진다. -> 내부 클래스로 정의해서 해결할 수 있다. 그리고 선언된 곳의 정보에 접근할 수 있다.(user)

``` java

/// 인터페이스
public class DeleteAllStatement implements StatementStrategy{
    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}

```

``` java

    StatementStrategy strategy = new DeleteAllStatement();
    jdbcContextWithStatementStrategy(strategy);

```

``` java
    // 로컬 클래스
    public void deleteAll() throws SQLException {
        class DeleteAllStatement implements StatementStrategy {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("delete from users");
                return ps;
            }
        }
        StatementStrategy st = new AddStatement();
        jdbcContextWithStatementStrategy(st); // 이 메서드는 나중에 jdbcContext로 분리
    }
         
``` 

``` java
public class JdbcContext {
    ...
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;
        try{
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        }catch (SQLException e){
            throw e;
        } finally {
            if(ps != null){ try{ ps.close(); } catch (SQLException e){ } }
            if(c != null){ try{ c.close(); } catch(SQLException e){ } }
        }
    }
}   
```
