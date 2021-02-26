package nio3.kbs.gsm_scan_server.Service;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.configuration.Planners;
import nio3.kbs.gsm_scan_server.factory.RouterFactory;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;
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

    @Autowired
    private SpeachFactory speachFactory;
    @Autowired
    private RouterFactory routerFactory;
    private ThreadPoolTaskScheduler taskScheduler;
    private List<MonitoringServiceSettings> monitoringServiceSettingsList=new ArrayList<>();
    private PeriodicTrigger periodicTrigger;

    public StationProcessingServiceImpl() {
        super();
    }
    @Override
    public void start() {
        state=true;
        taskScheduler=planners.taskScheduler();
        monitoringServiceSettingsList.clear();
        settings.getStantionList().forEach((stantion -> {
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
        }));
    }
    @Override
    public void stop() {
        state=false;
        settings.getStantionList().forEach((stantion -> {
            log.info("станция " + stantion.getName() + " остановлена....");
            taskScheduler.shutdown();
        }));
    }
    @PostConstruct
    private void init(){
        periodicTrigger = planners.getTrigger(settings.getTimeQuery());
    }


}
