package nio3.kbs.gsm_scan_server.algoritm;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.SpeachAlgoritmInfoDTO;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Log4j
@Data
public abstract class AbstractAlgoritm implements Algoritm {

    private Map<StantionSpeachDTO, SpeachAlgoritmInfoDTO> result;
    private Algoritm nextAlgoritm;
    public AbstractAlgoritm() {
        super();
    }

    @Override
    public void check(StantionSpeachDTO stantionSpeachDTOS) {
        if (result==null) result=new HashMap<>();
    }

    @Override
    public void setResult(Map<StantionSpeachDTO, SpeachAlgoritmInfoDTO> result) {
        this.result=result;
    }
    @Override
    public Map<StantionSpeachDTO, SpeachAlgoritmInfoDTO> getResult() {
        return this.result;
    }

    /**
     * Этот метод нужно переопределять в классах потомках
     */
    protected abstract void processEntry();
}
