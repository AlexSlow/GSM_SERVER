package nio3.kbs.gsm_scan_server.Service;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.MonitoringServiceSettings;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Page;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.SpeachRepository;
import nio3.kbs.gsm_scan_server.clients.Stantion;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Data
public class RunableSpeachMonitoring implements Runnable {

    private Page page=new Page(50);
    private List<Speach> speaches=new ArrayList<>();
    private Stantion stantion;
    private MonitoringServiceSettings monitoringServiceSettings;
    private SpeachRepository speachRepository;
    @Override
    public void run() {
        //1 Мы  должны определить настройки. ПОлучим последний id
        if (!monitoringServiceSettings.isInit()) {
            monitoringServiceSettings.setLastId(speachRepository.findLastId());
            log.debug("получен id " + monitoringServiceSettings.getLastId());
        }
     //Получить все записи, после текущего id

     boolean isEnd=false;
     while (!isEnd)
     {
         speaches.clear();
         speaches=speachRepository.findAllByIdMore(page,monitoringServiceSettings.getLastId());
         //Если страница дала не пустой резульат, то нужно записать последний id
         Long lastId=getLastId(speaches);
         if (lastId==MonitoringServiceSettings.EMPTY_ROW) {
             isEnd=true; //Страница пустая
             log.debug("Отсутствуют записи");
         }else
         {
             //Записан lastid
             log.debug("Новый lid= "+lastId);
             monitoringServiceSettings.setLastId(lastId);
         }
         System.out.println(speaches);
         //page.next();
     }
    }

    /**
     * Получить последний id в коллекции
     * @param list
     * @return
     */
    private Long getLastId(List<Speach> list)
    {
        if (!list.isEmpty()) return list.get(list.size()-1).getId();
        else return MonitoringServiceSettings.EMPTY_ROW;
    }
}
