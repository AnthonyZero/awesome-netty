package com.anthonyzero.example;

import com.anthonyzero.common.RPCService;

@RPCService(UserService.class)
public class UserServiceImpl implements UserService {

    public int getUserAge(String userName) {
        return 24;
    }

    public User getUser(String userName) {
        User user = new User();
        user.setAge(24);
        user.setUserName(userName);
        user.setPassword("123456");
        user.setSalary("15000");
        return user;
    }
}
