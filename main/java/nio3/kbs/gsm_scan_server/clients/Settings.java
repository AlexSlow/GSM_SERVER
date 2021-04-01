package nio3.kbs.gsm_scan_server.clients;


import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
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

    /**
     * Предназначен при инициализацц станций для перераспределения id
     * @param stantion
     */
    public void addAndGenerateId(Stantion stantion){
        stantion.setId(idCount++);
        stantionList.add(stantion);
    }

    /**
     * При обновлении станции. Если Id будет не пустым, то будет обновление.
     * @param stantion
     */

    public void add(@NonNull Stantion stantion){
        if (stantion.getId()==null) {
            stantion.setId(idCount++);
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
    public void addList(List<Stantion> stantions){
        if (stantions!=null){
            stantions.forEach(s->{
                this.add(s);
            });
        }else {
            log.warn("Список станций пуст!");
        }
    }

    public void addListWithGenerateId(List<Stantion> stantions){
        if (stantions!=null){
            stantions.forEach(s->{
                this.addAndGenerateId(s);
            });
        }
    }

    public Stantion getById(Integer id){
        for(Stantion stantion:stantionList) if (stantion.getId().equals(id)) return stantion;
        throw new RuntimeException("Отстуствует станция");
    }
    public void removeById(Integer id){
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
           settings.stantionList.forEach(s->s.setActive(false));
       this.addListWithGenerateId(settings.stantionList);
       this.TimeOut=settings.TimeOut;
       this.TimeQuery=settings.TimeQuery;
       log.info("Инициализация станций");
    }

    /**
     * Это следует убрать в следующих версиях.
     */
    public void save(){
        SettingsSerializator settingsSerializator=new SettingsSerializator();
        settingsSerializator.serialize(this);
    }

    /**
     * Нужно для сброса активности перед сериализаций (в будущем нужно оптимизировать)
     * Уже устарел, так как активность теперь не сериализуется
     * @param isActive
     */
    public void setActive(boolean isActive){
        stantionList.forEach(s->{
            s.setActive(isActive);
        });
    }

}
