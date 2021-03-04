package nio3.kbs.gsm_scan_server.Service;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.DtoRouter.DtoRouter;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.configuration.Planners;
import nio3.kbs.gsm_scan_server.factory.RouterFactory;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;
import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Log4j
@Service("StationMonitoring")
public class StationProcessingServiceImpl implements StationProcessingService {
    private boolean state=false;
    @Autowired
    Planners planners;
    @Autowired
    private Settings settings;
    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired private StantionToDtoFactory stantionToDtoFactory;
    @Autowired
    private SpeachFactory speachFactory;
    @Autowired
    private RouterFactory routerFactory;
    private ThreadPoolTaskScheduler taskScheduler;
    private List<MonitoringServiceSettings> monitoringServiceSettingsList=new ArrayList<>();
    private PeriodicTrigger periodicTrigger;
    //Тут хранятся представления станций.

    public StationProcessingServiceImpl() {
        super();
    }
    @Override
    public void start() {
        state=true;
        taskScheduler=planners.taskScheduler();
        monitoringServiceSettingsList.clear();

        settings.getStantionList().forEach((stantion -> {


            try {
                log.info("станция " +stantion.getName()+" запускается....");
                MonitoringServiceSettings monitoringServiceSettings=new MonitoringServiceSettings();
                //Для теста сканирования . Должно быть NOT_INIT
                monitoringServiceSettings.setLastId(MonitoringServiceSettings.NOT_INIT);
                //Добавить роутер
                monitoringServiceSettings.setDtoRouter(routerFactory.toClientAndAlgoritmRoute());
                monitoringServiceSettingsList.add(monitoringServiceSettings);
                RunableSpeachMonitoring runableSpeachMonitoring=new RunableSpeachMonitoring();
                //Добавить фабрику
                runableSpeachMonitoring.setSpeachFactory(speachFactory);
                runableSpeachMonitoring.setStantion(stantion);
                runableSpeachMonitoring.setMonitoringServiceSettings(monitoringServiceSettings);
                runableSpeachMonitoring.setSpeachRepository(connectionFactory.getRepository(stantion));
                taskScheduler.schedule(runableSpeachMonitoring,periodicTrigger);
               stantion.setActive(true);
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
                stantion.setActive(false);
            }finally {

            }
        }));
        //Отправить в чат сообщение о
      DtoRouter<List<StantionDto>> toStantionChat = routerFactory.toStantionChat();

    }
    @Override
    public void stop() {
        state=false;
        /*
        stantionDtos.forEach((stantion -> {
            log.info("станция " + stantion.getName() + " остановлена....");
            stantion.setActive(false);

        }));
        */
        taskScheduler.shutdown();
    }
    @PostConstruct
    private void init(){
        periodicTrigger = planners.getTrigger(settings.getTimeQuery());
    }


}
