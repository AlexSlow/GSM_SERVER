package nio3.kbs.gsm_scan_server.algoritm;
import nio3.kbs.gsm_scan_server.DTO.SpeachAlgoritmInfoDTO;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.clients.Stantion;

import java.util.List;
import java.util.Map;

public interface Algoritm {
    void check(List<StantionSpeachDTO> stantionSpeachDTOS);
    void setResult(Map<StantionSpeachDTO, SpeachAlgoritmInfoDTO> result);
    Map<StantionSpeachDTO, SpeachAlgoritmInfoDTO> getResult ();
}
