package nio3.kbs.gsm_scan_server.controller;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.PageStantionIdDto;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Page;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.SpeachRepository;
import nio3.kbs.gsm_scan_server.clients.ClientNotify;
import nio3.kbs.gsm_scan_server.clients.ClientServiceInterface;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;
import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Log4j
@RequestMapping(value="Speach")
@RestController
public class SpeachController {
    public static final  String GET_PAGE_SPEACH="page";
    public static final  String GET_PAGE_SPEACH_WITH_ID_LESS="pageWithIdLess";
    public static final  String GET_ALL_BY_PERIOD="getAllByPeriod";
    @Autowired
    private ClientServiceInterface clientService;
    @Autowired private volatile Settings settings;
    @Autowired private volatile ConnectionFactory connectionFactory;
    @Autowired private volatile SpeachFactory speachFactory;


    @PostMapping(value=GET_PAGE_SPEACH)
    public ResponseEntity<StantionSpeachDTO> getPage(@RequestBody PageStantionIdDto pageStantionIdDto){
        try {
          //  log.debug("Пришел запрос на страницу речи "+pageStantionIdDto.getPage()
           //        +" "+pageStantionIdDto.getStantionId() );

            /**
             * Требуется кэширование !!! Исправить в сл. версиях
             */

            Stantion stantion = settings.getById(pageStantionIdDto.getStantionId());


         SpeachRepository speachRepository=connectionFactory.getRepository(stantion);
         return new ResponseEntity<> (speachFactory.factory(speachRepository.
                 getPageOrderById(pageStantionIdDto.getPage()),stantion)
                 ,HttpStatus.OK);

        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value=GET_PAGE_SPEACH_WITH_ID_LESS,headers = {"Content-type=application/json"})
    public ResponseEntity<StantionSpeachDTO> getPageWithIdLess(@RequestBody PageStantionIdDto pageStantionIdDto,Long id){
        try {
       //    log.debug("Пришел запрос на страницу речи "+pageStantionIdDto.getPage()
        //            +" "+pageStantionIdDto.getStantionId()+" начиная с "+ pageStantionIdDto.getSpeachId() );

            Stantion stantion = settings.getById(pageStantionIdDto.getStantionId());
            SpeachRepository speachRepository=connectionFactory.getRepository(stantion);




            return new ResponseEntity<> (speachFactory.factory(speachRepository.getPageLessId
                    (pageStantionIdDto.getPage(),pageStantionIdDto.getSpeachId()),stantion)
                    ,HttpStatus.OK);

        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value=GET_ALL_BY_PERIOD)
    public ResponseEntity<StantionSpeachDTO> getAllByPeriod(@RequestParam(name = "start",required = false) Long start,
                                                 @RequestParam(name = "end", required = false) Long end,
                                                 @RequestParam(name = "stantionId",required = false) Long stantionId){

        try {
            Stantion stantion = settings.getById(stantionId);
            SpeachRepository speachRepository=connectionFactory.getRepository(stantion);

            return new ResponseEntity<> (speachFactory.factory(speachRepository.getAllByPeriod(new Date(start),new Date (end))
                    ,stantion), HttpStatus.OK);

        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
