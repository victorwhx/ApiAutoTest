package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListModel;
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
import java.util.List;

/*
 * 表getUserListCase中存储查询用户列表的条件，
 * 比如当前例子中查询sex=0即性别为男的用户
 * 对应mapper中的id：getUserListCase
 *
 * 从表user中查询用户具体信息
 * 对应mapper中的id：getUserList
 */
public class GetUserInfoListTest {
    @Test(dependsOnGroups = "loginTrue", description = "获取性别为'男'的用户信息")
    public void getUserListInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListModel getUserListModel = session.selectOne("getUserListCase", 1);
        System.out.println(getUserListModel.toString());
        System.out.println(TestConfig.getUserListUrl);

        // 下边为写完接口的代码
        JSONArray resultJson = getJsonResult(getUserListModel);

        Thread.sleep(2000);
        /*
         * 要查询的mapper文件的id存储在表getUserListCase的expected字段中
         * 传入一个getUserListModel对象，查询条件即存在该对象中，如本例的sex = 0
         * 在sql语句中通过对该对象的各个字段进行筛选来确定查询条件
         */
        List<UserModel> userList = session.selectList(getUserListModel.getExpected(), getUserListModel);
/*        for (UserModel u : userList) {
            System.out.println("list获取的user: " + u.toString());
        }*/
        JSONArray userListJson = new JSONArray(userList);
        Assert.assertEquals(userListJson.length(),resultJson.length());
        for(int i = 0;i<resultJson.length();i++){
            JSONObject expect = (JSONObject) resultJson.get(i);
            JSONObject actual = (JSONObject) userListJson.get(i);
            Assert.assertEquals(expect.toString(), actual.toString());
        }
    }

    private JSONArray getJsonResult(GetUserListModel getUserListModel) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
        if (getUserListModel.getUserName() != null) {
            param.put("userName", getUserListModel.getUserName());
        }
        if (getUserListModel.getAge() > 0) {
            param.put("age", getUserListModel.getAge());
        }
        param.put("sex", getUserListModel.getSex());
        post.setHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
//        System.out.println("result: " + result);
        JSONArray jsonArray = new JSONArray(result);
//        System.out.println("调用接口list result: " + result);

        return jsonArray;
    }
}
