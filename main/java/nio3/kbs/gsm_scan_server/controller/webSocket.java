package nio3.kbs.gsm_scan_server.controller;

import nio3.kbs.gsm_scan_server.clients.Client;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class webSocket {
    @MessageMapping("/addStantion")
    @SendTo("/topic/addStantion")
    public Stantion sendMessage( Stantion stantion) {
        return stantion;
    }
    // Нужно убрать. Регистрация не тут
    @MessageMapping("/addClient")
    @SendTo("/topic/addClient")
    public Client addClient (Client client,SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", client);
        return client;
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String test (@Payload String message,
                        Principal user) {
        //headerAccessor.getSessionAttributes().put("username", client);
        System.out.println(user);
        System.out.println(message);
        return "hello from server";
    }
}
