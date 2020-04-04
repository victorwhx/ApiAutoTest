package com.course.utils;

import com.course.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name) {
        String address = bundle.getString("test.url");
        String uri = "";
        String testUrl = "";

        if (name.equals(InterfaceName.GETUSERLIST)) {
            uri = bundle.getString("getUserList.uri");
        } else if (name.equals(InterfaceName.GETUSERINFO)) {
            uri = bundle.getString("getUserInfo.uri");
        } else if (name.equals(InterfaceName.LOGIN)) {
            uri = bundle.getString("login.uri");
        } else if (name.equals(InterfaceName.UPDATEUSERINFO)) {
            uri = bundle.getString("updateUserInfo.uri");
        } else if (name.equals(InterfaceName.ADDUSER)) {
            uri = bundle.getString("addUser.uri");
        }

        testUrl = address + uri;
        return testUrl;
    }
}
