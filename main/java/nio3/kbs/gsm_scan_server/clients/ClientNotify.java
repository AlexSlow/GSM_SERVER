package nio3.kbs.gsm_scan_server.clients;

import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;

import java.util.List;

/**
 * Оповещает подписчиков
 */
public interface ClientNotify {
    void sendAllUsers(List<Client> clients);
    void sendAllStantions(List<StantionDto> stantionDtos);
    void sendAllSpeach(StantionSpeachDTO stantionSpeachDTO);
}
