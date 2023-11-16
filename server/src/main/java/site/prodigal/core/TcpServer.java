package site.prodigal.core;

import site.prodigal.entity.Action;
import site.prodigal.entity.Result;
import site.prodigal.serialization.Protocol;
import site.prodigal.utils.ObjectUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    public static final Map<String,PrintWriter> users = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("等待客户端连接...");
        ExecutorService pool = Executors.newFixedThreadPool(20);
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接!");

            pool.execute( () -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("客户端发送: " + inputLine);
                        Action action = (Action) Protocol.toObject(inputLine,Action.class);
                        //处理请求
                        Object handle = RequestHandler.handle(action);

                        Result res = new Result();
                        res.setCallback(action.getCallback());

                        //登录请求特殊处理
                        if ("/login".equals(action.getPath()) && (Boolean)handle){
                            users.put((String) action.getParams()[0],out);
                        }

                        //处理消息发送请求
                        if (ObjectUtils.isNotEmpty(action.getDestination())) {
                            res.setData(Protocol.toJsonStr(action.getParams()[0]));
                            //TODO 添加新的字段来判断是群聊消息还是私发消息
                            if("测试群聊".equals(action.getDestination())){
                                users.forEach((k,v)->{
                                    // 发送消息给目标用户
                                    v.println(Protocol.toJsonStr(res));
                                });
                            }else {
                                if (users.containsKey(action.getDestination())) {
                                    // 发送消息给目标用户
                                    users.get(action.getDestination()).println(Protocol.toJsonStr(res));
                                }else {
                                    // 发送消息给自己
                                    out.println(Protocol.toJsonStr(res));
                                }
                            }

                        }else{
                            //处理正常请求
                            res.setData(Protocol.toJsonStr(handle));
                            out.println(Protocol.toJsonStr(res));
                        }

                    }

                    //TODO clear disconnected client

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException ignored) {}
                }
            });
        }

    }

}
