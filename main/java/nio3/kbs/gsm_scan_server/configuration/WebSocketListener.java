package nio3.kbs.gsm_scan_server.configuration;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.clients.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Log4j
@Component
public class WebSocketListener {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
       // String username = (String) headerAccessor.getSessionAttributes().get("username");
        System.out.println(headerAccessor);
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("simpUser");
        System.out.println(headerAccessor);
        if(username != null) {
            log.info("User Disconnected : " + username);
            Client client = new Client();
             client.setName(username);
            messagingTemplate.convertAndSend("/topic/removeUser", client);
        }
    }


}
