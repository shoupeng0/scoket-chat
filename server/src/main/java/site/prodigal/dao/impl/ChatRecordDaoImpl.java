package site.prodigal.dao.impl;

import site.prodigal.dao.ChatRecordDao;
import site.prodigal.entity.ChatRecord;
import site.prodigal.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 9:54
 * @description a
 * @email 7698627@qq.com
 */
public class ChatRecordDaoImpl implements ChatRecordDao {

    @Override
    public Boolean insertRecord(ChatRecord record) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "INSERT INTO chat_record (send_username,receive_username,message) VALUES (?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, record.getSendUsername());
            stmt.setString(2, record.getReceiveUsername());
            stmt.setString(3, record.getMessage());
            int res = stmt.executeUpdate();
            return res > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtils.release(conn, stmt, null);
        }
    }

    @Override
    public List<ChatRecord> getRecordList(String sendUsername, String receiveUsername) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ChatRecord> records = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();
            if ("测试群聊".equals(receiveUsername)){
                String sql = "select * from chat_record where receive_username = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, receiveUsername);
            }else {
                String sql = "select * from chat_record where (send_username = ? and receive_username = ?) or (send_username = ? and receive_username = ?)";
                stmt = conn.prepareStatement(sql);

                stmt.setString(1, sendUsername);
                stmt.setString(2, receiveUsername);
                stmt.setString(3, receiveUsername);
                stmt.setString(4, sendUsername);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String sendUsernameDB = rs.getString("send_username");
                String receiveUsernameDB = rs.getString("receive_username");
                String message = rs.getString("message");
                ChatRecord record = new ChatRecord(id,sendUsernameDB,receiveUsernameDB,message);
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.release(conn, stmt, rs);
        }

        return records;
    }

}
