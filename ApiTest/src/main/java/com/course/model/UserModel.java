package com.course.model;

import lombok.Data;

@Data
public class UserModel {
    private int id;
    private String userName;
    private String password;
    private int age;
    private int sex;
    private int permission;
    private int isDelete;

    @Override
    public String toString() {
        return (
                "[{\"id\":" + id + "," +
                "\"userName\":\"" + userName + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"age\":" + age + "," +
                "\"sex\":" + sex + "," +
                "\"permission\":" + permission + "," +
                "\"isDelete\":" + isDelete + "}]"
                );
    }
/*    @Override
    public String toString(){
        return (
                "{id:"+id+","+
                        "userName:"+userName+","+
                        "password:"+password+","+
                        "age:"+age+","+
                        "sex:"+sex+","+
                        "permission:"+permission+","+
                        "isDelete:"+isDelete+"}"
        );
    }*/
}
