package nio3.kbs.gsm_scan_server.controller;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.Response;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.clients.*;
import nio3.kbs.gsm_scan_server.configuration.Topics;
import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequestMapping(value="gServer")
@RestController
@Log4j
public class ClientAndStantionRestController {

    public static final String SUBSCRIBE="subscribe";
    public static final String UNSUBSCRIBE="unsubscribe";
    public static final String GET_ALL="getStantions";
    public static final String GET_SUBSCRIBE="getSubscribe";
    public static final String ADD_STANTION="AddStantion";
    public static final String REMOVE_STANTION="RemoveStantion/{id}";
    public static final String TEST="test";
    public static final String GET_STANTION_BY_ID="getStantion/{id}";


    @Autowired
    private volatile ClientServiceInterface clientService;
    @Autowired private Settings settings;
    @Autowired private StantionToDtoFactory stantionToDtoFactory;
    @Autowired private ClientNotify clientNotify;


    @PutMapping(value=ADD_STANTION,headers = {"Content-type=application/json"})
    public ResponseEntity addStantion(@RequestBody Stantion stantion){
        try {
            log.debug("Пришел запрос на добавлении станции "+stantion);
            settings.add(stantion);
            settings.save();
           clientNotify.sendAllStantions(stantionToDtoFactory.factory(settings.getStantionList()));
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = REMOVE_STANTION)
    public ResponseEntity removeStantion(@PathVariable Integer id)
    {
        try {
            log.debug("Пришел запрос на удаление станции "+id);
            settings.removeById(id);
            settings.save();
            clientNotify.sendAllStantions(stantionToDtoFactory.factory(settings.getStantionList()));
            return new ResponseEntity(HttpStatus.OK);

        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = SUBSCRIBE)
    public ResponseEntity subscribe (
            @RequestParam(name = "stantionId",required = false) Integer stantionDtoId,
            @RequestParam(name = "userUUID",required = false)  String userUUID){
        log.info("Запрос на подписку "+userUUID+" "+stantionDtoId);
        try {
            clientService.subscribe(userUUID,stantionDtoId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(UNSUBSCRIBE)
    public ResponseEntity unsubscribe (String userUUID){
        try {
            clientService.unsubscribe(userUUID);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Возвращает все подписки клиентов
     * @return
     */
    @GetMapping(GET_SUBSCRIBE)
    public ResponseEntity<Map<Client,StantionDto>> getSubscribe (){
        try {
            return new ResponseEntity<>( clientService.getSubscribe(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(GET_ALL)
    public ResponseEntity<List<StantionDto>> getAllStantions (){
        try {
            return new ResponseEntity<>(stantionToDtoFactory.factory(settings.getStantionList()),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(GET_STANTION_BY_ID)
    public ResponseEntity<Stantion> getStantionById(@PathVariable Integer id){
        try {
            return new ResponseEntity<>(settings.getById(id),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
