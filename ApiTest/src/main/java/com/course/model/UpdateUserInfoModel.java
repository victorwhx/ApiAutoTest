package com.course.model;

import lombok.Data;

@Data
public class UpdateUserInfoModel {
    private int id;
    private int userId;
    private String userName;
    private int age;
    private int sex;
    private int permission;
    private int isDelete;
    private String expected;
}
