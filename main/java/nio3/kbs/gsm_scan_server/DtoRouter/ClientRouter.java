package nio3.kbs.gsm_scan_server.DtoRouter;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.algoritm.AlgoritmManager;
import nio3.kbs.gsm_scan_server.clients.ClientNotify;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j
@Data
public class ClientRouter extends DtoRouter<StantionSpeachDTO>{
private ClientNotify clientNotify;

    @Override
    public void route(StantionSpeachDTO stantionSpeachDTOS) {
            if (this.getNextRouter()!=null) this.getNextRouter().route(stantionSpeachDTOS);
            clientNotify.sendAllSpeach(stantionSpeachDTOS);
    }
}
