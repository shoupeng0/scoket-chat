package site.prodigal.dao;

import site.prodigal.entity.User;

import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 9:31
 * @description 用户相关
 * @email 7698627@qq.com
 */
public interface UserDao {

    Boolean login(String username,String password);

    List<User> getUserList();

}
