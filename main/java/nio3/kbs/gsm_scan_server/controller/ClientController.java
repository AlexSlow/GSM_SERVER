package nio3.kbs.gsm_scan_server.controller;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.clients.Client;
import nio3.kbs.gsm_scan_server.clients.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Log4j
@RestController
@RequestMapping(value="client")
public class ClientController  {
    private static final String GET_ALL_CLIENTS="get_all";
    private
    @Autowired
    ClientService clientService;
    @GetMapping(GET_ALL_CLIENTS)
    ResponseEntity<List<Client>> getAll(){
        try {
            return new ResponseEntity<>(clientService.getClients(), HttpStatus.OK);
        }catch (Exception e){
            log.warn(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
