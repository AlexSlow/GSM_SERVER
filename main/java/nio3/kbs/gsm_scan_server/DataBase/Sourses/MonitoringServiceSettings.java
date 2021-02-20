package nio3.kbs.gsm_scan_server.DataBase.Sourses;

import lombok.Data;

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
}
