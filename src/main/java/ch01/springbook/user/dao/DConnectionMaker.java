package ch01.springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {


    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        //D사의 독자전인 방법으로 connection 생성하는 코드
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/springbook?characterEncoding=UTF-8", "spring", "book");
        return c;
    }
}
