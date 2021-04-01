package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Для работы с клиентами.
 * Так же данному классу делегирована функция оповещения подписчиков.
 * В будущем такая схема должна быть преведена в соответствии с принципами SOLID
 */
@Component
@Data
@Log4j
public class ClientService implements ClientServiceInterface {
    /**
     * ПОдписки клиентов (на станции)
     */
    private volatile Map<Client, StantionDto> clientStantionMap;
    private List<Client> clients;
    @Autowired private StantionToDtoFactory stantionToDtoFactory;
    @Autowired private Settings settings;
    @Override
    public Optional<Client> getClientByUUID(String UUID){
        for(Client client:clients) if (client.getUUID().equals(UUID)) return Optional.of(client);
        return Optional.empty();
    }

    @Override
    public void addInformationOboutClient(String UUID,Client clientDto) {
        log.info("Уточнение информации о клиенте "+UUID+" "+clientDto);
        Optional<Client> client=getClientByUUID(UUID);
        if (client.isPresent())
        {

          client.get().setInfo(clientDto);
        }else {
            log.error("Ошибка при записи информации о клиенте клиента.Нет пользователя с таким UUID.");
        }
    }


    @Override
    public  void addUser(Client client){
        log.info("Добавлен новый пользователь "+ client);
        clients.add(client);
    }
    @Override
    public void addUser(String UUID){
        Client client=new Client(UUID);
        addUser(client);
    }
    @Override
    public void remove(String UUID){
        Optional<Client> client=getClientByUUID(UUID);
       if(client.isPresent()){
           log.info("Отключился  пользователь "+client);
           //Сначала нужно убрать подписки
           unsubscribe(UUID);
           clients.remove(client.get());
        }else {
           log.error(" Ошибка при удалении клиента. Нет пользователя с таким UUID.");
       }

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void subscribe(String UUID, Integer stantionDtoId) {

        StantionDto stantionDto =stantionToDtoFactory.factory(settings.getById(stantionDtoId));
        log.info("Запрос на подписку пользователя "+UUID+" на станцию "+stantionDto.getName());
        Optional<Client> client=getClientByUUID(UUID);
        if (client.isPresent())
        {
           clientStantionMap.put(client.get(),stantionDto);
           System.out.println(clientStantionMap);
        }else {
            log.error("Ошибка при подписке клиента.Нет пользователя с таким UUID.");
        }
    }

    @Override
    public void unsubscribe(String UUID) {
        log.debug("Ициализация отказа от  подписки клиента "+UUID);
        Optional<Client> client=getClientByUUID(UUID);
        if (client.isPresent())
        {
            //Проверить, есть ли такая подписка
            StantionDto subscriberStantionPast=clientStantionMap.get(client.get());
            if (subscriberStantionPast!=null)
           {
                log.debug("Процесс отписки завершен");
                clientStantionMap.remove(client.get());

            }else {
                log.warn("Попытка отменить отписку, которой нет. Пользователь " + client.get().getName());
            }

        }else {
            log.warn("Ошибка при отписке клиента.Нет пользователя с таким UUID.");
        }
    }

    @Override
    public Map<Client, StantionDto> getSubscribe() {
        return this.clientStantionMap;
    }

    @PostConstruct
    public void init(){
      clients=new ArrayList<>();
      clientStantionMap=new ConcurrentHashMap<>();
    }
}

