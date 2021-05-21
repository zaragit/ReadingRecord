package spring.user.dao;

import spring.user.domain.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDao {
//    private ConnectionMaker connectionMaker;
//
//    public UserDao(){
//        connectionMaker = new DConnectionMaker(); //문제가 될 부분 -> 불필요한 의존 관계
//
//    }
//    public void add(User user) throws ClassNotFoundException, SQLException{
//        Connection c = connectionMaker.makeConnection();
//    }
//    public void get(String id) throws ClassNotFoundException,SQLException{
//        Connection c = connectionMaker.makeConnection();
//    }

    //Singleton Pattern
    //1. static 영역에 객체를 딱 1개만 생성해둔다.
    private static final UserDao instance = new UserDao();

    //2. public으로 열어서 객체 인스터스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한 다.
    public static UserDao getInstance() {
        return instance;
    }

    //3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다
    private UserDao() {
    }

}
}
