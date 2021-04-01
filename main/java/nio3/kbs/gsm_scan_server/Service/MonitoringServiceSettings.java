package nio3.kbs.gsm_scan_server.Service;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DtoRouter.DtoRouter;
import nio3.kbs.gsm_scan_server.clients.ClientNotify;

import java.util.concurrent.ScheduledFuture;

@Data
public class MonitoringServiceSettings {
    public static Long NOT_INIT=-2l;
    public static Long EMPTY_ROW=-1l;
    private Long lastId=NOT_INIT;
    public void reset()
    {
        lastId=NOT_INIT;
    }
    public boolean isInit(){
        return lastId!=NOT_INIT;
    }
    private DtoRouter dtoRouter;
    private CustomErrorHandler customErrorHandler;

}
