package nio3.kbs.gsm_scan_server.DtoRouter;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import nio3.kbs.gsm_scan_server.DTO.StantionSpeachDTO;
import nio3.kbs.gsm_scan_server.algoritm.AlgoritmManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j
@Data
public class ClientRouter extends DtoRouter<List<StantionSpeachDTO>>{
   // private AlgoritmManager algoritmManager;

    @Override
    public void route(List<StantionSpeachDTO> stantionSpeachDTOS) {
          //  if (algoritmManager==null) {log.warn("Отстутствует менеджер клиентов!");return;}
          //  CompletableFuture.runAsync(()->{algoritmManager.processByAlgorithm(stantionSpeachDTOS);});
            if (this.getNextRouter()!=null) this.getNextRouter().route(stantionSpeachDTOS);
    }
}
