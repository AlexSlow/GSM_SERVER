package nio3.kbs.gsm_scan_server.factory;

import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpeachFactory {
    @Autowired private StantionToDtoFactory stantionToDtoFactory;
  public   StantionSpeachDTO factory(List<Speach> speaches, Stantion stantion)
    {
        StantionSpeachDTO stantionSpeachDTO=new StantionSpeachDTO();
        stantionSpeachDTO.setSpeachList(speaches);
        stantionSpeachDTO.setStantionDto(stantionToDtoFactory.factory(stantion));
        return stantionSpeachDTO;
    }
    /*
    public List<StantionSpeachDTO> factory(List<Speach> speaches,Stantion stantion)
    {
        List<StantionSpeachDTO> stantionSpeachDTOS=new ArrayList<>();
        speaches.forEach((sp)->{
            stantionSpeachDTOS.add(factory(sp,stantion));
        });
        return stantionSpeachDTOS;
    }
     */
}
