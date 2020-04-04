package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoModel;
import com.course.model.UserModel;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {
    @Test(dependsOnGroups = "loginTrue", description = "获取user id为1的用户信息")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoModel getUserInfoModel = session.selectOne("getUserInfoCase", 1);
        System.out.println(getUserInfoModel.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        // 以下为写完接口后的测试代码
        JSONArray resultJson = getJsonResult(getUserInfoModel);
        Thread.sleep(1500);
//        UserModel userModel = session.selectOne(getUserInfoModel.getExpected(), getUserInfoModel);
        UserModel userModel = session.selectOne(getUserInfoModel.getExpected(), getUserInfoModel.getUserId());
        System.out.println("查询数据库获取的用户信息：" + userModel.toString());

        // 该方法得到的jsonArry的值顺序不对
/*        List<UserModel> userList = new ArrayList<>();
        userList.add(userModel);
        JSONArray jsonArray1 = new JSONArray(userList);
        System.out.println("test: " + jsonArray1.toString());*/

        List resultList = Arrays.asList(userModel.toString());
        JSONArray jsonArray = new JSONArray(resultList);

//        System.out.println("数据库获取用户信息:"+jsonArray.toString());
//        System.out.println("调用接口获取用户信息:"+resultJson.toString());
        Assert.assertEquals(jsonArray,resultJson);
    }

    private JSONArray getJsonResult(GetUserInfoModel getUserInfoModel) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id", getUserInfoModel.getUserId());
        post.setHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        List resultList = Arrays.asList(result);
        JSONArray array = new JSONArray(resultList);
        return array;
    }
}
