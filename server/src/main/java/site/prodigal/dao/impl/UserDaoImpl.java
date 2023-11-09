package site.prodigal.dao.impl;

import site.prodigal.dao.UserDao;
import site.prodigal.entity.ChatRecord;
import site.prodigal.entity.User;
import site.prodigal.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 9:47
 * @email 7698627@qq.com
 */

public class UserDaoImpl implements UserDao {

    @Override
    public Boolean login(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM user WHERE username=? and password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.release(conn, stmt, rs);
        }
        return false;
    }

    @Override
    public List<User> getUserList() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from user";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                User user = new User(id,username,null);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.release(conn, stmt, rs);
        }

        return users;
    }


}
