package nio3.kbs.gsm_scan_server.clients;

import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;

import java.util.List;

public interface ClientNotify {
    void getUsers();
    void getStantions();
    void getSpeach(List<StantionSpeachDTO> stantionSpeachDTOS);
}
