package site.prodigal.service;

import site.prodigal.entity.User;

import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 10:31
 * @email 7698627@qq.com
 */
public interface UserService {

    Boolean login(String username,String password);

    List<User> getUserList();

}
