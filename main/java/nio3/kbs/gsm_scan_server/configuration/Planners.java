package nio3.kbs.gsm_scan_server.configuration;

import nio3.kbs.gsm_scan_server.DataBase.Sourses.ConnectionFactory;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.SpeachRepository;
import nio3.kbs.gsm_scan_server.clients.ClientNotify;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import nio3.kbs.gsm_scan_server.clients.StantionStatus;
import nio3.kbs.gsm_scan_server.factory.StantionToDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class Planners {
@Autowired
private volatile   Settings settings;
@Autowired private ConnectionFactory  connectionFactory;
@Autowired private ClientNotify clientNotify;
@Autowired private StantionToDtoFactory stantionToDtoFactory;
    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("STANTION -");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setRemoveOnCancelPolicy(true);

        scheduler.initialize();
        return scheduler;
    }
    public PeriodicTrigger getTrigger(Integer time)
    {
        return new PeriodicTrigger(time, TimeUnit.SECONDS);
    }

    /**
     * Проверка на доступность подключенных станций
     */
    @Scheduled(fixedDelay = 90000)
    public void checkStantionOnAvalible() {

        List<Stantion> stantions = settings.getStantionList();
        boolean update=false;
        for (Stantion s:stantions)
        {
            if (s.getStatus().equals(StantionStatus.Online)) continue;
            // Есть ли проблемы
            boolean err=false;
            try {
                connectionFactory.getRepository(s).ping();
               // getRepository(s).ping();
            }catch (Exception e)
            {
            err=true;
            }finally {
                if ((err)&&(s.getStatus().equals(StantionStatus.Offline)))
                {
                    s.setStatus(StantionStatus.notAvailable);
                    update=true;
                }else if ((!err)&&(s.getStatus().equals(StantionStatus.notAvailable))) {
                    s.setStatus(StantionStatus.Offline);
                    update=true;
                }
            }

        }
        if (update) clientNotify.sendAllStantions(stantionToDtoFactory.factory(
                settings.getStantionList()
        ));
    }

}
