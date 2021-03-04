package nio3.kbs.gsm_scan_server.controller;

import nio3.kbs.gsm_scan_server.clients.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class TestController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/testPost")
    public ResponseEntity<Client> test (@RequestBody Client client){
        System.out.println(client);
        try {

            return new ResponseEntity<>(new Client("123"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/testPostParam")
    public ResponseEntity<Client> testPostParam ( @RequestParam(name="param",required = false) Client param){
        System.out.println(param);
        try {

            return new ResponseEntity<>(new Client("123"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/testGet")
    public ResponseEntity<String> testGet (@RequestParam(name="data",required = false) Client string){
        System.out.println("Тест Get "+string);
        try {

            return new ResponseEntity<String>("Hello  from server",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>("Error",HttpStatus.BAD_REQUEST);
        }
    }

    @MessageMapping("/test")
    public void test (@Payload Client message,
                      Principal user) {
        //headerAccessor.getSessionAttributes().put("username", client);
        System.out.println("Пришло сообщение от "+user+" "+message);

        // simpMessagingTemplate.convertAndSend("/topic/test","hello from server");
        Client websocketClient=new Client();
        websocketClient.setUUID("Вася");
        simpMessagingTemplate.convertAndSend("/topic/test","test_connection");
    }
}
