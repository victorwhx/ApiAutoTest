package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoModel;
import com.course.model.UserModel;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

public class UpdateUserInfoTest {
    @Test(dependsOnGroups = "loginTrue", description = "更改用户信息")
    public void updateUserInfo() throws IOException, InterruptedException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        UpdateUserInfoModel updateUserInfoModel = sqlSession.selectOne("updateUserInfoCase", 1);
        sqlSession.close();
        System.out.println(updateUserInfoModel.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        // 下边为写完接口的代码
        int result = getResult(updateUserInfoModel);
        Thread.sleep(2000);

/*        // 接口未实现时，手动添加测试数据
        updateUserInfoModel.setUserName("zhao2");
        updateUserInfoModel.setAge(50);
        updateUserInfoModel.setSex(1);   
        updateUserInfoModel.setPermission(1);
        updateUserInfoModel.setIsDelete(0);*/

        /*
         * 数据更新后，从user表中查询已更新的数据。
         * 需要先关闭上一次查询时创建的session，再创建一个新的session
         * 否则查询的数据是未更新的数据，即使数据库的内容已经更新。
         */
        sqlSession = DatabaseUtil.getSqlSession();
        UserModel userModel = sqlSession.selectOne(updateUserInfoModel.getExpected(), updateUserInfoModel);
        System.out.println(userModel.toString());
    }

    private int getResult(UpdateUserInfoModel updateUserInfoModel) throws IOException {
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject(new LinkedHashMap());
        param.put("id",updateUserInfoModel.getUserId());
        param.put("userName",updateUserInfoModel.getUserName());
        param.put("sex",updateUserInfoModel.getSex());
        param.put("age",updateUserInfoModel.getAge());
        param.put("permission",updateUserInfoModel.getPermission());
        param.put("isDelete",updateUserInfoModel.getIsDelete());
        post.setHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        return Integer.parseInt(result);
    }
}
