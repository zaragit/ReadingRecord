package com.spring.bomstart.user.dao;

import com.spring.bomstart.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("devandy");
        user.setName("youngjinmo");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId()+" 등록 성공");

    }
}
