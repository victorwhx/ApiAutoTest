package com.course.model;

import lombok.Data;

@Data
public class AddUserModel {
    private int id;
    private String userName;
    private String password;
    private int age;
    private int sex;
    private int permission;
    private int isDelete;
    private String expected;
}
