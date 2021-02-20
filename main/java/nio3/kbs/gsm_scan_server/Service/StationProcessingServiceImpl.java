package nio3.kbs.gsm_scan_server.Service;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.MonitoringServiceSettings;
import nio3.kbs.gsm_scan_server.clients.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
public class StationProcessingServiceImpl implements StationProcessingService {
    private boolean state=false;
    @Autowired
    private Settings settings;

    @Autowired
    private ConnectionFactory connectionFactory;

    private ThreadPoolTaskScheduler taskScheduler= this.taskScheduler();
    private List<MonitoringServiceSettings> monitoringServiceSettingsList=new ArrayList<>();
    private PeriodicTrigger periodicTrigger;

    public StationProcessingServiceImpl() {
        super();

    }

    @Override
    public void start() {
        state=true;
        monitoringServiceSettingsList.clear();
        settings.getStantionList().forEach((stantion -> {
            log.info("станция " +stantion.getName()+" запускается....");
            MonitoringServiceSettings monitoringServiceSettings=new MonitoringServiceSettings();

            //Для теста сканирования
            monitoringServiceSettings.setLastId(MonitoringServiceSettings.EMPTY_ROW);

            monitoringServiceSettingsList.add(monitoringServiceSettings);
            RunableSpeachMonitoring runableSpeachMonitoring=new RunableSpeachMonitoring();
            runableSpeachMonitoring.setStantion(stantion);
            runableSpeachMonitoring.setMonitoringServiceSettings(monitoringServiceSettings);
            runableSpeachMonitoring.setSpeachRepository(connectionFactory.getRepository(stantion));
           // taskScheduler.scheduleWithFixedDelay(runableSpeachMonitoring,settings.getTimeQuery());
            taskScheduler.schedule(runableSpeachMonitoring,periodicTrigger);
        }));

    }

    @Override
    public void stop() {
        state=false;

    }
    @PostConstruct
    private void init(){
        periodicTrigger = new PeriodicTrigger(settings.getTimeQuery(),TimeUnit.SECONDS);
    }

    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("STANTION -");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.initialize();
        return scheduler;
    }
}
