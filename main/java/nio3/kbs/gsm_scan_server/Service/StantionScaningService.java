package nio3.kbs.gsm_scan_server.Service;


import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import nio3.kbs.gsm_scan_server.factory.RouterFactory;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

/**
 * Сервис сканирует все записи БД постранично
 */
@Data
@Log4j
public class StantionScaningService implements StantionProcessingService {
    private Stantion stantion;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private RouterFactory routerFactory;
    @Autowired
    private SpeachFactory speachFactory;
    private Future future;
    public StantionScaningService() {
        super();
    }

    @Override
    public void start() {
if (stantion==null) {
    log.warn("Станция не определена для сканирвания!");
    return;
}
RunableSpeachMonitoring runableSpeachMonitoring=new RunableSpeachMonitoring();
MonitoringServiceSettings monitoringServiceSettings=new MonitoringServiceSettings();
monitoringServiceSettings.setLastId(MonitoringServiceSettings.EMPTY_ROW);
//Формирование маршрута
monitoringServiceSettings.setDtoRouter(routerFactory.toAlgoritm());

        monitoringServiceSettings.setCustomErrorHandler(new CustomErrorHandler() {
            @Override
            public void onError(Exception e) {
                log.warn("Исключение в потоке при сканировании");

            }
        });

runableSpeachMonitoring.setMonitoringServiceSettings(monitoringServiceSettings);
runableSpeachMonitoring.setSpeachRepository(connectionFactory.getRepository(stantion));
//Добавить фабрику
runableSpeachMonitoring.setSpeachFactory(speachFactory);
future=CompletableFuture.runAsync(runableSpeachMonitoring);
runableSpeachMonitoring.setFuture(future);
    }
    @Override
    public void stop() {
    future.cancel(true);
    }
}
