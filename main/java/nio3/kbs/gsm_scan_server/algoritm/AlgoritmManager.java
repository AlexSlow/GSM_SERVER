package nio3.kbs.gsm_scan_server.algoritm;

import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlgoritmManager {


    public void processByAlgorithm(List<StantionSpeachDTO> stantionSpeachDTOS){

        System.out.println(" менеджер алгоритмов "+stantionSpeachDTOS);
    }
}
