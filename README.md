# scoket-chat-awt
课程小项目-简易聊天

#execute this sql
```sql
  create database chat;
  use chat;
  create table user(
    id int primary key auto_increment,
    username varchar(30) not null,
    password varchar(100) not null
  )auto_increment = 1;
  
  create table chat_record(
       id int primary key auto_increment,
       send_username varchar(100) not null,
       receive_username varchar(100) not null,
       message varchar(100) not null
  )auto_increment = 1;
  
  insert into user values (1,'7698627','123456');
  insert into user values (2,'123456','123456');
```
# edit the mysql's config in StringConstants at server project
```java
  public class StringConstants {
  
      public static final String URL = "jdbc:mysql://localhost:3306/chat";
      public static final String USERNAME = "root";
      public static final String PASSWORD = "root";
  
  }
```
# then run TcpServer
# and then run LoginUI
