package site.prodigal.ui;

import lombok.*;
import lombok.experimental.Accessors;
import site.prodigal.client.TcpClient;
import site.prodigal.entity.Action;
import site.prodigal.entity.ChatRecord;
import site.prodigal.entity.User;
import site.prodigal.serialization.Protocol;
import site.prodigal.utils.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ChatUI {

    private String username;
    private TcpClient client;

    public ChatUI(String username,TcpClient client){
        this.username = username;
        this.client = client;
        JFrame frame = new JFrame(username);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //左侧用户列表
        DefaultListModel<String> listModel = new DefaultListModel<>();
        //拉取所有用户
        Action action = new Action("/getUserList",null,new Object[]{});
        listModel.addElement("测试群聊");
        client.sendMsg(Protocol.toJsonStr(action));
        List<User> users = Protocol.toObjectAsListUserReference(client.receiveMsg().replace("\r\n", ""));
        for (User user : users) {
            listModel.addElement(user.getUsername());
        }

        JList<String> userList = new JList<>(listModel);

        List<ChatRecord> records = null;
        if (ObjectUtils.isNotEmpty(users)){
            userList.setSelectedIndex(0);
            client.setCurrentSelectUser(userList.getSelectedValue());
            //拉取和列表第一个用户的聊天记录
            records = getChatRecord(userList.getSelectedValue());
        }

        // 右侧聊天框
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        //渲染聊天内容
        renderingChatArea(chatArea,records);

        //左侧用户选择监听
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                client.setCurrentSelectUser(userList.getSelectedValue());
                renderingChatArea(chatArea, getChatRecord( userList.getSelectedValue() ));
            }
        });

        userList.setForeground(Color.GREEN);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(100, 0));

        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        // 消息输入框和发送按钮
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("发送");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = messageField.getText();
                if (ObjectUtils.isEmpty(msg)){
                    JOptionPane.showMessageDialog(frame, "请输入想要发送的消息....");
                    return;
                }
                Action action = new Action("/insertRecord",userList.getSelectedValue(),new Object[]{new ChatRecord(null,username,userList.getSelectedValue(),messageField.getText())});
                client.sendMsg(Protocol.toJsonStr(action));
                chatArea.append("我: " + messageField.getText() + "\n");
                messageField.setText("");
            }
        });


        // 将组件添加到面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(userScrollPane, BorderLayout.WEST);
        panel.add(chatScrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(messageField, BorderLayout.CENTER);
        southPanel.add(sendButton, BorderLayout.EAST);

        panel.add(southPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        //开始监听消息
        client.startListening(chatArea,username);
    }

    private List<ChatRecord> getChatRecord(String selectUsername){
        Action action = new Action();
        //拉取和列表第一个用户的聊天记录
        action.setPath("/getRecordList");
        action.setParams(new Object[]{username,selectUsername});

        client.sendMsg(Protocol.toJsonStr(action));

        return Protocol.toObjectAsListRecordReference(client.receiveMsg().replace("\r\n", ""));
    }

    private void renderingChatArea(JTextArea chatArea,List<ChatRecord> records){
        chatArea.setText("");
        if (ObjectUtils.isNotEmpty(records)){
            for (ChatRecord record : records) {
                if (record.getSendUsername().equals(username)){
                    chatArea.append("我: " + record.getMessage() + "\n");
                }else {
                    chatArea.append(record.getSendUsername() + "：" + record.getMessage() + "\n");
                }
            }
        }
    }

}
