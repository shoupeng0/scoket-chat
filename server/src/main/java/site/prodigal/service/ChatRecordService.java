package site.prodigal.service;

import site.prodigal.entity.ChatRecord;

import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 10:33
 * @email 7698627@qq.com
 */
public interface ChatRecordService {

    Boolean insertRecord(ChatRecord record);

    List<ChatRecord> getRecordList(String sendUsername, String receiveUsername);

}
