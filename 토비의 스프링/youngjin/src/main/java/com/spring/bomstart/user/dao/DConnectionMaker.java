package com.spring.bomstart.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/bomstart";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1q2w3e4r";

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
