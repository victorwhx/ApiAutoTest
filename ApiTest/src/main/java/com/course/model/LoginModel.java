package com.course.model;

import lombok.Data;

@Data
public class LoginModel {
    private int id;
    private String userName;
    private String password;
    private String expected;
}
