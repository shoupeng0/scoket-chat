package site.prodigal.service.impl;

import site.prodigal.dao.ChatRecordDao;
import site.prodigal.dao.impl.ChatRecordDaoImpl;
import site.prodigal.entity.ChatRecord;
import site.prodigal.service.ChatRecordService;

import java.util.List;

/**
 * @author ShouPeng
 * @date 2023/11/8 10:34
 * @email 7698627@qq.com
 */
public class ChatRecordServiceImpl implements ChatRecordService {
    private static final ChatRecordDao chatRecordDao = new ChatRecordDaoImpl();

    @Override
    public Boolean insertRecord(ChatRecord record) {
        return chatRecordDao.insertRecord(record);
    }

    @Override
    public List<ChatRecord> getRecordList(String sendUsername, String receiveUsername) {
        return chatRecordDao.getRecordList(sendUsername, receiveUsername);
    }
}
