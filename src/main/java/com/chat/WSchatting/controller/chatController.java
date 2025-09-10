package com.chat.WSchatting.controller;


import com.chat.WSchatting.model.chatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class chatController {

    // 클라이언트가 "/app/send" 로 보내면 → "/topic/messages" 로 브로드캐스트
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public chatMessage send(chatMessage message) {
        return message;
    }
}