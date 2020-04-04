package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginModel;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {
    @BeforeTest(groups = "loginTrue", description = "测试准备工作,获取HttpClient对象")
    public void beforeTest() {
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSER);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);

        TestConfig.cookieStore = new BasicCookieStore();
        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
    }

    @Test(groups = "loginTrue", description = "用户成功登录接口")
    public void loginTrue() throws IOException {
        // 从数据库中查询loginCase表中id为1的数据
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginModel loginModel = session.selectOne("loginCase", 1);
        System.out.println(loginModel.toString());
        System.out.println(TestConfig.loginUrl);

        // 下边的代码为写完接口的测试代码
        String result = getResult(loginModel);
        Assert.assertEquals(loginModel.getExpected(), result);
    }

    @Test(description = "用户登录失败接口")
    public void loginFalse() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginModel loginModel = session.selectOne("loginCase", 2);
        System.out.println(loginModel.toString());
        System.out.println(TestConfig.loginUrl);

        // 下边的代码为写完接口的测试代码
        String result = getResult(loginModel);
        Assert.assertEquals(loginModel.getExpected(), result);
    }

    private String getResult(LoginModel loginModel) throws IOException {
        // 下边的代码为写完接口的测试代码
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("userName", loginModel.getUserName());
        param.put("password", loginModel.getPassword());
        // 设置请求头信息 设置header
        post.setHeader("content-type", "application/json");
        // 将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        // 执行post方法
        HttpResponse response = TestConfig.httpClient.execute(post);
        // 获取响应结果
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

/*        List<Cookie> cookieList = TestConfig.cookieStore.getCookies();
        for (Cookie cookie : cookieList) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("cookieName: " + name + ", cookieValue: " + value);
        }*/

        return result;
    }
}
