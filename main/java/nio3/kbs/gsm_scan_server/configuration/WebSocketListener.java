package nio3.kbs.gsm_scan_server.configuration;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.clients.Client;
import nio3.kbs.gsm_scan_server.clients.ClientNotify;
import nio3.kbs.gsm_scan_server.clients.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Log4j
@Component
public class WebSocketListener {

    @Autowired private ClientService clientService;
    @Autowired private ClientNotify clientNotify;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal username = headerAccessor.getUser();
        if(username != null) {
            clientService.addUser(username.getName());
        }

    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal username = headerAccessor.getUser();
        if(username != null) {
            clientService.remove(username.getName());
        }
        clientNotify.sendAllUsers(clientService.getAll());
    }


}
