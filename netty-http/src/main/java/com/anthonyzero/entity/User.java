package com.anthonyzero.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private String userName;

    private String city;

    private Date date;

    private String method;
}
