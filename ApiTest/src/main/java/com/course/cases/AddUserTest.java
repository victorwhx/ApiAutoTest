package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.AddUserModel;
import com.course.model.UserModel;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 要插入的数据存储在addusercase表中
 * 操作流程是先将addusercase表中的数据取出，然后再插入进user表
 * 这应该是具体接口要实现的功能
 * 接口测试代码只实现查询数据，不实现具体的插入数据操作
 */
public class AddUserTest {
    @Test(dependsOnGroups = "loginTrue", description = "添加用户接口")
    public void addUser() throws InterruptedException, IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        AddUserModel addUserModel = session.selectOne("addUserCase", 1);
        session.close();
        System.out.println(addUserModel.toString());
        System.out.println(TestConfig.addUserUrl);

        // 下边的代码是写完接口后的测试代码
        // 发请求，获取结果
        String result = getResult(addUserModel);

        // 验证结果
        /*
         * 从user表中查询新添加的数据
         * 真正的插入数据动作由实际接口实现
         */
        Thread.sleep(2000);
        session = DatabaseUtil.getSqlSession();

        List<UserModel> addedUserList = new ArrayList<>();
        // 不能用selectOne()，因为如果多次添加相同的数据，查询时会返回多个结果。
        addedUserList.addAll(session.selectList("addUser", addUserModel));
/*        for (int i = 0; i < addedUserList.size(); i++) {
            System.out.println(addedUserList.get(i).toString());
        }*/

        Assert.assertEquals(addUserModel.getExpected(), result);
    }

    private String getResult(AddUserModel addUserModel) throws IOException {
        // 下边的代码为写完接口的测试代码
        HttpPost post = new HttpPost(TestConfig.addUserUrl);
        JSONObject param = new JSONObject();
        param.put("userName", addUserModel.getUserName());
        param.put("password", addUserModel.getPassword());
        param.put("age", String.valueOf(addUserModel.getAge()));
        param.put("sex", String.valueOf(addUserModel.getSex()));
        param.put("permission", String.valueOf(addUserModel.getPermission()));
        param.put("isDelete", String.valueOf(addUserModel.getIsDelete()));
        // 设置请求头信息
        post.setHeader("content-type", "application/json");
        // 将参数信息添加到post方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        // 设置cookie

        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        return result;
    }
}