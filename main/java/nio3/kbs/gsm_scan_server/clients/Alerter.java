package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@Log4j
public class Alerter implements ClientNotify {
    @Autowired  private ClientService clientService;
    @Override
    public void getUsers() {

    }

    @Override
    public void getStantions() {

    }

    @Override
    public void getSpeach(List<StantionSpeachDTO> stantionSpeachDTOS) {

    }
}
