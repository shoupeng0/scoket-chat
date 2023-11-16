package site.prodigal.ui;

import lombok.*;
import lombok.experimental.Accessors;
import site.prodigal.client.TcpClient;
import site.prodigal.entity.Action;
import site.prodigal.entity.ChatRecord;
import site.prodigal.entity.Result;
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

    public String username;

    public JTextArea chatArea;

    public ChatUI(String username){
        System.out.println("init chatUI....");
        init(username);
    }

    private JList<String> userList;

    private void init(String username){
        this.username = username;
        JFrame frame = new JFrame(username);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //左侧用户列表
        DefaultListModel<String> listModel = new DefaultListModel<>();
        //拉取所有用户
        Action action = new Action("/getUserList",null,new Object[]{});
        listModel.addElement("测试群聊");
        LoginUI.client.sendMsg(Protocol.toJsonStr(action));
        Result res = (Result)Protocol.toObject(LoginUI.client.receiveMsg(), Result.class);
        List<User> users = Protocol.toObjectAsListUserReference(res.getData().replace("\r\n", ""));
        for (User user : users) {
            listModel.addElement(user.getUsername());
        }

        userList = new JList<>(listModel);

        List<ChatRecord> records = null;
        if (ObjectUtils.isNotEmpty(users)){
            userList.setSelectedIndex(0);
            //拉取和列表第一个用户的聊天记录
            records = getChatRecord(userList.getSelectedValue());
        }

        // 右侧聊天框
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        //渲染聊天内容
        renderingChatArea(chatArea,records);

        //左侧用户选择监听
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
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
                action.setCallback("chatUI");
                LoginUI.client.sendMsg(Protocol.toJsonStr(action));
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

    }

    private List<ChatRecord> getChatRecord(String selectUsername){
        Action action = new Action();
        //拉取和列表第一个用户的聊天记录
        action.setPath("/getRecordList");
        action.setParams(new Object[]{username,selectUsername});

        LoginUI.client.sendMsg(Protocol.toJsonStr(action));
        Result res = (Result)Protocol.toObject(LoginUI.client.receiveMsg(), Result.class);
        return Protocol.toObjectAsListRecordReference(res.getData());
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

    public void callback(Result res){
        // 处理消息
        ChatRecord record = (ChatRecord) Protocol.toObject(res.getData().replace("\r\n",""), ChatRecord.class);

        //排除自己发给自己的消息
        if (!record.getSendUsername().equals(username)){

            //渲染到聊天界面上
            if ((userList.getSelectedValue().equals(record.getSendUsername()) && (!record.getReceiveUsername().equals("测试群聊")))
                    || (userList.getSelectedValue().equals("测试群聊") && record.getReceiveUsername().equals("测试群聊"))){
                chatArea.append(record.getSendUsername() + "：" + record.getMessage() + "\n");
            }
        }
    }

}
