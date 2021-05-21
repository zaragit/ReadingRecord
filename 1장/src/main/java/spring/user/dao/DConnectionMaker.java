package spring.user.dao;

import spring.user.dao.ConnectionMaker;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        return null; //D사의 독자적인 방법으로 Connection을 생성하는 코드 작성하면 됨
    }
}
