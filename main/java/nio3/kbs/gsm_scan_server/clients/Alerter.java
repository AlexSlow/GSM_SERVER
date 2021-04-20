package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.configuration.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Data
@Log4j
public class Alerter implements ClientNotify {
    @Autowired volatile   private ClientService clientService;
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendAllUsers(List<Client> clients) {
        simpMessagingTemplate.convertAndSend(Topics.TOPIC_CLIENTS,clientService.getClients());
    }

    @Override
    public void sendAllStantions(List<StantionDto> stantionDtos) {
    simpMessagingTemplate.convertAndSend(Topics.TOPIC_STANTION_DTO,stantionDtos);
    }

    @Override
    public void sendAllSpeach(StantionSpeachDTO stantionSpeachDTO) {

      //  System.out.println("Отправка пакета "+stantionSpeachDTO.getStantionDto().getName());
      //  simpMessagingTemplate.convertAndSend(Topics.TOPIC_SPEACH,stantionSpeachDTOS);

        if (stantionSpeachDTO.getSpeachList().size()==0) return;
         Map<Client, StantionDto> clientStantionMap = clientService.getClientStantionMap();
         List<Client> clients=getClients(clientService.getClientStantionMap(),
                 stantionSpeachDTO.getStantionDto());


         clients.forEach(c->{simpMessagingTemplate.
                 convertAndSendToUser(c.getUUID(),Topics.TOPIC_SPEACH,stantionSpeachDTO);});
    }
    private   List<Client> getClients(Map<Client,StantionDto> map,StantionDto stantionDto){
        List<Client> clients = new ArrayList<>();

        for(Map.Entry<Client, StantionDto> entry : map.entrySet()) {

            if (entry.getValue().equals(stantionDto)) clients.add(entry.getKey());
        }
        return clients;
    }
}
