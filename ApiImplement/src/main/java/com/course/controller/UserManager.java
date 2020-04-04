package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
@Api(value = "v1")
@RequestMapping("v1")
public class UserManager {
    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @PostMapping(value = "/login")
    public Boolean login(HttpServletResponse response, @RequestBody User user) {
        int i = template.selectOne("login", user);
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        System.out.println("查询到的结果是：" + i);
        if (i == 1) {
            System.out.println("登录的用户是：" + user.getUserName());
            return true;
        } else {
            return false;
        }
    }

    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @PostMapping("/addUser")
    public boolean addUser(HttpServletRequest request, @RequestBody User user) {
        Boolean x = verifyCookies(request);
        int result = 0;
        if (x != null) {
            result =  template.insert("addUser", user);
        }
        if (result > 0) {
            System.out.println("添加用户的数量是：" + result);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户(列表)信息接口", httpMethod = "POST")
    @PostMapping(value = "/getUserInfo")
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user) {
        Boolean x = verifyCookies(request);
        if (x == true) {
            List<User> users = template.selectList("getUserInfo", user);
            System.out.println("getUserInfo获取到的用户数量是：" + users.size());
            return users;
        } else {
            return null;
        }
    }

    @ApiOperation(value = "更新/删除用户接口", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    public int updateUser(HttpServletRequest request, @RequestBody User user) {
        Boolean x = verifyCookies(request);
        int i = 0;
        if (x == true) {
            i = template.update("updateUserInfo", user);
        }
        System.out.println("更新数据的条目数为：" + i);
        return i;
    }

    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            System.out.println("cookie is null");
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                System.out.println("cookie is ok");
                return true;
            }
        }
        return false;
    }
}
