package site.prodigal.client;


import site.prodigal.entity.Result;
import site.prodigal.serialization.Protocol;
import site.prodigal.callback.CallbackCenter;
import site.prodigal.utils.ObjectUtils;

import java.io.*;
import java.net.Socket;

public class TcpClient {
    private Socket socket;

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

    @Deprecated
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

    public void startListening() {
        Thread listenerThread = new Thread(() -> {
            System.out.println("start listening....");
            try {
                while (true) {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[4096];
                    if (inputStream.available() > 0) {
                        int length = inputStream.read(buffer);
                        if (length == -1) {
                            // 连接已关闭
                            break;
                        }

                        String message = new String(buffer);
                        Result res = (Result) Protocol.toObject(message, Result.class);
                        System.out.println("Received message: " + res);

                        //处理消息
                        if (ObjectUtils.isNotEmpty(res.getCallback())){
                            Object obj = CallbackCenter.get(res.getCallback());
                            obj.getClass().getMethod("callback",Result.class).invoke(obj,res);
                        }

                    } else {
                        // 避免频繁地检查
                        Thread.sleep(300);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
    }

}
