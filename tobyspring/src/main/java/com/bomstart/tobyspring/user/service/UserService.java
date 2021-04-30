package com.bomstart.tobyspring.user.service;

import com.bomstart.tobyspring.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
