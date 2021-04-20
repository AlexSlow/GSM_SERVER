package nio3.kbs.gsm_scan_server.Service;

import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.DtoRouter.DtoRouter;
import nio3.kbs.gsm_scan_server.clients.ClientNotify;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.StantionStatus;
import nio3.kbs.gsm_scan_server.configuration.Planners;
import nio3.kbs.gsm_scan_server.factory.RouterFactory;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;
import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;


@Log4j
@Service("StationMonitoring")
public class StantionProcessingServiceImpl implements StantionProcessingService {
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
    @Autowired
    private ClientNotify clientNotify;

    public StantionProcessingServiceImpl() {
        super();
    }
    @Override
    public void start() {
        if (state) return;
        state=true;

        taskScheduler=planners.taskScheduler();
        monitoringServiceSettingsList.clear();

        settings.getStantionList().forEach((stantion -> {
            try {
                log.info("станция " +stantion.getName()+" запускается....");
                MonitoringServiceSettings monitoringServiceSettings=new MonitoringServiceSettings();
                //Для теста сканирования.   Должно быть NOT_INIT
                monitoringServiceSettings.setLastId(MonitoringServiceSettings.NOT_INIT);
                monitoringServiceSettings.setCustomErrorHandler(new CustomErrorHandler() {
                    @Override
                    public void onError(Exception e) {
                        log.warn("Исключение в потоке "+e.getLocalizedMessage());
                        clientNotify.sendAllStantions(stantionToDtoFactory.
                                factory(settings.getStantionList()));
                        //Нужно остановить задачу
                    }
                });
                //Добавить роутер
                monitoringServiceSettings.setDtoRouter(routerFactory.toClientAndAlgoritmRoute());
                monitoringServiceSettingsList.add(monitoringServiceSettings);
                RunableSpeachMonitoring runableSpeachMonitoring=new RunableSpeachMonitoring();

                //Добавить фабрику
                runableSpeachMonitoring.setSpeachFactory(speachFactory);
                runableSpeachMonitoring.setStantion(stantion);
                runableSpeachMonitoring.setMonitoringServiceSettings(monitoringServiceSettings);



                runableSpeachMonitoring.setSpeachRepository(connectionFactory.getRepository(stantion));


                      ScheduledFuture future=taskScheduler.schedule(runableSpeachMonitoring,periodicTrigger);
                      runableSpeachMonitoring.setFuture(future);

               stantion.setStatus(StantionStatus.Online);
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
                stantion.setStatus(StantionStatus.notAvailable);

            }finally {

            }
        }));

        clientNotify.sendAllStantions(stantionToDtoFactory.factory(settings.getStantionList()));

    }
    @Override
    public void stop() {
        state=false;
        taskScheduler.shutdown();

        settings.getStantionList().forEach(s->{
            if (s.getStatus().equals(StantionStatus.Online))
            s.setStatus(StantionStatus.Offline);
        });
        clientNotify.sendAllStantions(stantionToDtoFactory.factory(settings.getStantionList()));
    }

    @Override
    public Boolean getStatus() {
        return state;
    }

    @PostConstruct
    private void init(){
        periodicTrigger = planners.getTrigger(settings.getTimeQuery());
       // periodicTrigger = planners.getTrigger(30);


    }


}
