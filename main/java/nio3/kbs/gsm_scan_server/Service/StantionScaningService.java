package nio3.kbs.gsm_scan_server.Service;


import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import nio3.kbs.gsm_scan_server.factory.RouterFactory;
import nio3.kbs.gsm_scan_server.factory.SpeachFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Сервис сканирует все записи БД постранично
 */
@Data
@Log4j
public class StantionScaningService implements StationProcessingService {
    private Stantion stantion;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private RouterFactory routerFactory;
    @Autowired
    private SpeachFactory speachFactory;
    private CompletableFuture completableFuture;
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
runableSpeachMonitoring.setMonitoringServiceSettings(monitoringServiceSettings);
runableSpeachMonitoring.setSpeachRepository(connectionFactory.getRepository(stantion));
//Добавить фабрику
runableSpeachMonitoring.setSpeachFactory(speachFactory);
completableFuture=CompletableFuture.runAsync(runableSpeachMonitoring);
    }
    @Override
    public void stop() {
    completableFuture.cancel(true);
    }
}
