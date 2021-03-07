package ch01.springbook.user.dao;

import ch01.springbook.user.domain.User;
import java.sql.*;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker simpleConnectionMaker){
        this.connectionMaker =  simpleConnectionMaker;
    }

    public void create() throws SQLException, ClassNotFoundException {
        Connection c = this.connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE USERS (ID VARCHAR(10) PRIMARY KEY, NAME VARCHAR (20) NOT NULL, PASSWORD VARCHAR(10) NOT NULL)");

        ps.executeUpdate();

        ps.close();
        c.close();

    }


    public void add(User user) throws SQLException, ClassNotFoundException {
        Connection c = this.connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();

    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = this.connectionMaker.makeConnection();
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
