package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        System.out.println("SimpleConnectionMaker connection Start");
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection("jdbc:h2:~/test:AUTO_SERVER=true", "sa", "sa");
        System.out.println("Connection Object return");
        return c;
    }
}
