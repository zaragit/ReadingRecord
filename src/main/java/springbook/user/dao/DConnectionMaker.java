package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {


    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        //D사의 독자전인 방법으로 connection 생성하는 코드
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection("jdbc:h2:~/test:AUTO_SERVER=true", "sa", "sa");
        return c;
    }
}
