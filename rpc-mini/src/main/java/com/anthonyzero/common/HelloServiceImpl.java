package com.anthonyzero.common;

public class HelloServiceImpl implements HelloService {
    public String hello(String name) {
        return name != null ? name + " -----> I am fine." : "I am fine.";
    }
}
