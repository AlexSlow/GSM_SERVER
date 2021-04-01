package nio3.kbs.gsm_scan_server.controller;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.Response;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.clients.Client;
import nio3.kbs.gsm_scan_server.clients.ClientServiceInterface;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.Stantion;

import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller
@Log4j
public class webSocket {
    public static final String ADD_INFORMATION_ABOUT_CLIENT="/addInformationOboutClient";


    public static final String SET_CLIENT_INFORMATION="/queue/getClient";


    @Autowired private ClientServiceInterface clientService;
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * Сервер получает информацию о пользователю и возвращает его uuid
     * @param message
     * @param user
     */

    @MessageMapping(ADD_INFORMATION_ABOUT_CLIENT)
    public void addInformationOboutClient (@Payload Client message,
                               Principal user){
        try {
            clientService.addInformationOboutClient(user.getName(),message);
            Optional<Client> client=clientService.getClientByUUID(user.getName());
         //   System.out.println(client);
            simpMessagingTemplate.convertAndSendToUser(user.getName(),
            SET_CLIENT_INFORMATION,client.get());

        }catch (Exception e){
            log.error(e.getMessage());
          //  return Response.ERROR;
        }
    }


}
