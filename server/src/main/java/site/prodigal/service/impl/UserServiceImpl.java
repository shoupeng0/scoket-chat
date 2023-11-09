package site.prodigal.service.impl;

import site.prodigal.dao.UserDao;
import site.prodigal.dao.impl.UserDaoImpl;
import site.prodigal.entity.User;
import site.prodigal.service.UserService;

import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 10:32
 * @description a
 * @email 7698627@qq.com
 */
public class UserServiceImpl implements UserService {
    public static final UserDao userDao = new UserDaoImpl();

    @Override
    public Boolean login(String username, String password) {
        return userDao.login(username, password);
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }
}
