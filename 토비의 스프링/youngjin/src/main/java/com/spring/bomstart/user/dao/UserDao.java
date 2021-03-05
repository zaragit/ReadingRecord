package com.spring.bomstart.user.dao;

import com.spring.bomstart.user.domain.User;

import java.sql.*;

public class UserDao {
    private static final String DRIVER = "";
    private static final String URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public void add(User user) throws ClassNotFoundException, SQLException{
        Class.forName(DRIVER);
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO USER(id, name, password) VALUES(?,?,?)"
        );
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM USER WHERE id = ?"
        );
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }
}
