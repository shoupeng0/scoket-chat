package site.prodigal.client;


import site.prodigal.entity.ChatRecord;
import site.prodigal.serialization.Protocol;

import javax.swing.*;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient {
    private Socket socket;

    private String currentSelectUser;
    private Boolean isStart = false;

    public void setCurrentSelectUser(String currentSelectUser) {
        this.currentSelectUser = currentSelectUser;
    }

    public TcpClient(){
        try {
            socket = new Socket("localhost", 8080);
            System.out.println("已连接到服务器!");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendMsg(String msg){
        try {
            System.out.println("send:::" + msg);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public String receiveMsg(){
        try{
            // 接收响应
            byte[] buffer = new byte[8192];
            int length = socket.getInputStream().read(buffer);
            return new String(buffer, 0, length);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void startListening(JTextArea chatArea,String currentUser) {
        isStart = true;
        Thread listenerThread = new Thread(() -> {
            try {
                byte[] buffer = new byte[8192];
                while (true) {
                    InputStream inputStream = socket.getInputStream();
                    if (inputStream.available() > 0 && isStart) {
                        int length = inputStream.read(buffer);
                        if (length == -1) {
                            // 连接已关闭
                            break;
                        }
                        String message = new String(buffer, 0, length);
                        System.out.println("Received message: " + message);
                        // 处理接收到的消息
                        ChatRecord record = (ChatRecord) Protocol.toObject(message.replace("\r\n",""), ChatRecord.class);

                        //排除自己发给自己的消息
                        if (!record.getSendUsername().equals(record.getReceiveUsername())){

                            //渲染到聊天界面上
                            if (currentSelectUser.equals(record.getSendUsername())){
                                if (record.getSendUsername().equals(currentUser)){
                                    chatArea.append("我: " + record.getMessage() + "\n");
                                }else {
                                    chatArea.append(record.getSendUsername() + "：" + record.getMessage() + "\n");
                                }
                            }
                        }
                    } else {
                        // 避免频繁地检查
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
    }

}
