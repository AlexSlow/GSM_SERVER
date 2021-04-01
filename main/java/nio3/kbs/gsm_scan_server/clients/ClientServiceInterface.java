package nio3.kbs.gsm_scan_server.clients;

import nio3.kbs.gsm_scan_server.DTO.StantionDto;

import java.util.Map;
import java.util.Optional;

/**
 * Crud интерфейс для клиентов + подписки
 */
public interface ClientServiceInterface {
    Optional<Client> getClientByUUID(String UUID);
    void addInformationOboutClient(String UUID,Client clientDto);
    void addUser(Client client);
    public void addUser(String UUID);
    public void remove(String UUID);
    void subscribe(String UUID, Integer stantionDtoId);
    void unsubscribe(String UUID);
    Map<Client,StantionDto> getSubscribe();
}
