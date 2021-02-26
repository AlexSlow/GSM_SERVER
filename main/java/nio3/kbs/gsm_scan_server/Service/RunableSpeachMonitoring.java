package nio3.kbs.gsm_scan_server.Service;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Page;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.SpeachRepository;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Вид службы определяется настройками
 */
@Log4j
@Data
public class RunableSpeachMonitoring implements Runnable {

    private Page page=new Page(50);
    private List<Speach> speaches=new ArrayList<>();
    private Stantion stantion;
    private MonitoringServiceSettings monitoringServiceSettings;
    private SpeachRepository speachRepository;
    private SpeachFactory speachFactory;
    @Override
    public void run() {
        //1 Мы  должны определить настройки. ПОлучим последний id (Для службы сканирования)
      init();
     //Получить все записи, после текущего id
     boolean isEnd=false;
     while (!isEnd)
     {
         speaches.clear();
         speaches=getSpeaches();
         isEnd=checkIfEndOrFindLastId();
         giveSpeachesForProcessing(speaches);
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
    protected void init(){
        if (!monitoringServiceSettings.isInit()) {
            monitoringServiceSettings.setLastId(speachRepository.findLastId());
            log.debug("получен id " + monitoringServiceSettings.getLastId());
        }
    }
    protected List<Speach> getSpeaches(){
        return speachRepository.findAllByIdMore(page,monitoringServiceSettings.getLastId());
    }
    protected boolean checkIfEndOrFindLastId(){
        /*
        //ПРоверка на прерывание потока
        if (Thread.interrupted()) {
            log.info("прерывание потока ...");
            return false;
        }
        */
        Long lastId=getLastId(speaches);
        if (lastId==MonitoringServiceSettings.EMPTY_ROW) {
          //  log.debug("Отсутствуют записи");
            return true; //Страница пустая

        }else
        {
            //Записан lastid
           // log.debug("Новый lid= "+lastId);
            monitoringServiceSettings.setLastId(lastId);
        }
        return false;
    }

    private void giveSpeachesForProcessing(List<Speach> speaches){
        //Упаковка данных в DTO для обработки
        System.out.println(speaches);
        List<StantionSpeachDTO> stantionSpeachDTOS= speachFactory.factory(speaches,stantion);
    }


}
