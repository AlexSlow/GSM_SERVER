package nio3.kbs.gsm_scan_server.DtoRouter;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;

import java.util.List;
@Data
public  abstract  class DtoRouter {
    private DtoRouter nextRouter;
    public abstract void route(List<StantionSpeachDTO> stantionSpeachDTOS);
}
