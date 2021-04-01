package nio3.kbs.gsm_scan_server.controller;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.Service.StantionProcessingService;
import nio3.kbs.gsm_scan_server.clients.Client;
import nio3.kbs.gsm_scan_server.configuration.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Log4j
@RestController
@RequestMapping(value="Service")
public class ServiceController {
    private static final String START="start";
    private static final String STOP="stop";

    @Autowired private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private StantionProcessingService stantionProcessingService;
    @GetMapping(START)
    ResponseEntity<?> start(){
        try {
            stantionProcessingService.start();
            simpMessagingTemplate.convertAndSend(Topics.TOPIC_MONITORING_STATUS,true);
            return new ResponseEntity<>( HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(STOP)
    ResponseEntity<?> stop(){
        try {
            log.info("Остановка мониторинга");
            stantionProcessingService.stop();
            simpMessagingTemplate.convertAndSend(Topics.TOPIC_MONITORING_STATUS,false);
            return new ResponseEntity<>( HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
