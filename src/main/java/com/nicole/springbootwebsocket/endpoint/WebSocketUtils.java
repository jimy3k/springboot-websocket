package com.nicole.springbootwebsocket.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class WebSocketUtils {

    public static final Map<String, Session> CLIENTS = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtils.class);

    private WebSocketUtils() {
    }

    public static String getOnLineInfo() {
        Set<String> userNames = CLIENTS.keySet();
        if (userNames.size() == 0) {
            return "当前无人在线....";
        }
        return CLIENTS.keySet().toString() + "在线";
    }

    public static void sendMessageAll(String message) {
        //这里提供了一种快速遍历Map集合的代码,值得学习
        CLIENTS.forEach((username, session) -> sendMessage(session, message));
    }


    public static void sendMessage(Session session, String message) {
        if (session == null) {
            return;
        }
        //核心：通过session向前端页面发送数据，前端将会触发message事件
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return;
        }
        try {
            //核心：后台发送数据到前台，触发前台的Message事件
            basic.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("sendMessage IOException ", e);
        }

    }
}
