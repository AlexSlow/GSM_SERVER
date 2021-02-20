package nio3.kbs.gsm_scan_server.clients;


import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.clients.stationSerializator.SettingsSerializator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
@Log4j
@Component
@Data
public class Settings {

    private int TimeOut;
    private int TimeQuery;

    private static Integer idCount=0;
    private List<Stantion> stantionList=new ArrayList<>();

    public void add(@NonNull nio3.kbs.gsm_scan_server.clients.Stantion stantion){
        stantion.setId(idCount++);
        stantionList.add(stantion);

    }

    public void removeById(@NonNull Stantion stantion){
        AtomicReference<Stantion> deleted = new AtomicReference<>();
        stantionList.forEach((stn)->{if (stn.getId()==stantion.getId()) deleted.set(stn);});
        stantionList.remove(deleted.get());
    }
    @PostConstruct
    public void init(){
       SettingsSerializator settingsSerializator=new SettingsSerializator();
       Settings settings= settingsSerializator.deserialize();
       if (settings==null) return;
       if (settings.stantionList!=null)
       this.stantionList=settings.stantionList;
       this.TimeOut=settings.TimeOut;
       this.TimeQuery=settings.TimeQuery;

       log.info("Инициализация станций");
     //  System.out.println(this);
    }



}
