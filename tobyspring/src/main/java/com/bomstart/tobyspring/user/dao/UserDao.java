package com.bomstart.tobyspring.user.dao;

import com.bomstart.tobyspring.user.domain.User;

import java.util.List;

public interface UserDao {
    public void deleteAll();
    public User get(String id);
    public List<User> getAll();
    public int getCount();
    public void add(User user);
    public void update(User user);
}
