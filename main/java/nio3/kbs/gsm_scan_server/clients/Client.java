package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;

@Data
public class Client {
    private String name;
    private Integer lon;
    private Integer lat;
}
