package site.prodigal.dao;

import site.prodigal.entity.ChatRecord;

import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 9:31
 * @description 聊天记录
 * @email 7698627@qq.com
 */
public interface ChatRecordDao {

    Boolean insertRecord(ChatRecord record);

    List<ChatRecord> getRecordList(String sendUsername,String receiveUsername);

}
