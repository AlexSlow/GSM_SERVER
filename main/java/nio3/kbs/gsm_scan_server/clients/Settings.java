package nio3.kbs.gsm_scan_server.clients;


import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.clients.stationSerializator.SettingsSerializator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
@Log4j
@Component
@Data
public class Settings {

    private int TimeOut;
    private int TimeQuery;

    private  Long idCount;
    private List<Stantion> stantionList= Collections.synchronizedList(new ArrayList<>());

    /**
     * При обновлении станции. Если Id будет не пустым, то будет обновление.
     * @param stantion
     */

    public void add(@NonNull Stantion stantion){
        if (stantion.getId()==null) {
            stantion.setId(++idCount);
            stantionList.add(stantion);
        }else {
            Stantion oldStantion=getById(stantion.getId());
           if (oldStantion!=null) {
               oldStantion.setName(stantion.getName());
               oldStantion.setFile(stantion.getFile());
               oldStantion.setHost(stantion.getHost());
               oldStantion.setTypeConnection(stantion.getTypeConnection());
               oldStantion.setPassword(stantion.getPassword());
               oldStantion.setUsername(stantion.getUsername());
               oldStantion.setCoord_X(stantion.getCoord_X());
               oldStantion.setCoord_Y(stantion.getCoord_Y());
            }
        }

    }

    public Stantion getById(Long id){
        for(Stantion stantion:stantionList) if (stantion.getId().equals(id)) return stantion;
        throw new RuntimeException("Отстуствует станция");
    }
    public void removeById(Long id){
        remove(getById(id));
    }
    public void remove(@NonNull Stantion stantion){
        AtomicReference<Stantion> deleted = new AtomicReference<>();
        stantionList.forEach((stn)->{if (stn.getId()==stantion.getId()) deleted.set(stn);});
        stantionList.remove(deleted.get());
    }
    public  void  clear(){
        stantionList.clear();
    }
    @PostConstruct
    public void init(){
       SettingsSerializator settingsSerializator=new SettingsSerializator();
       Settings settings= settingsSerializator.deserialize();
       if (settings==null) return;
       if (settings.stantionList!=null)
           settings.stantionList.forEach(s->s.setStatus(StantionStatus.notAvailable));
       this.setStantionList(settings.getStantionList());
       this.TimeOut=settings.TimeOut;
       this.TimeQuery=settings.TimeQuery;
       this.idCount=settings.idCount;
       log.info("Инициализация станций");
    }

    /**
     * Это следует убрать в следующих версиях.
     */
    public void save(){
        SettingsSerializator settingsSerializator=new SettingsSerializator();
        settingsSerializator.serialize(this);
    }


}
