package nio3.kbs.gsm_scan_server.factory;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DTO.StantionDto;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
@Component
@Data
public class StantionToDtoFactory {
    public StantionDto factory(Stantion stantion){
        StantionDto stantionDto=new StantionDto();
        stantionDto.setActive(stantion.isActive());
        stantionDto.setId(stantion.getId());
        stantionDto.setName(stantion.getName());
        return stantionDto;
    }
    public List<StantionDto> factory(List<Stantion> stantions){
        List<StantionDto> stantionDtos=new ArrayList<>();
        stantions.forEach((stantion -> {
            stantionDtos.add(factory(stantion));
        }));
        return stantionDtos;
    }
}
