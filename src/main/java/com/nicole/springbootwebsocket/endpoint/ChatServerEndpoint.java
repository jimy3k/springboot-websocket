package com.nicole.springbootwebsocket.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Slf4j
@Component
@ServerEndpoint("/websocket/{username}")
public class ChatServerEndpoint {

    /*
     * 建立连接时触发
     */
    @OnOpen
    public void openSession(@PathParam("username") String username, Session session) {
        log.info("用户{}登录", username);
        String message = "用户[" + username + "] 已进入聊天室";
        //发送登录消息给其他人
        WebSocketUtils.sendMessageAll(message);
        //获取当前在线人数，发给自己
        String onlineInfo = WebSocketUtils.getOnLineInfo();
        //发送消息
        WebSocketUtils.sendMessage(session, onlineInfo);
        //添加自己到map中
        WebSocketUtils.CLIENTS.put(username, session);
    }

    @OnMessage
    public void onMessage(@PathParam("username") String username, String message) {
        log.info("发送消息：{}, {}", username, message);
        //发送消息给其他人
        WebSocketUtils.sendMessageAll("用户[" + username + "]：" + message);
    }

    @OnClose
    public void OnClose(@PathParam("username") String username, Session session) {
        //从当前session移除某个用户
        WebSocketUtils.CLIENTS.remove(username);
        //离开时消息通知所有人
        WebSocketUtils.sendMessageAll("用户[" + username + "]：" + "已离开！");
        try {
            //关闭Websocket Session会话
            session.close();
            log.info("{}已退出，onClose", username);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onClose error", e);
        }
    }

    @OnError
    public void OnError(Session session, Throwable throwable) {
        try {
            //关闭Websocket Session会话
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onClose error", e);
        }
        log.info("Throwable msg " + throwable.getMessage());
    }
}
