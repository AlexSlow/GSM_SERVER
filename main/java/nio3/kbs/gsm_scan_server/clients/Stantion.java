package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.TypeConnection;

import java.math.BigInteger;

@Data
public class Stantion {
    private Integer id;
    private String name;
   private String username;
   private String password;
   private String host;
   private String file;
   private TypeConnection typeConnection;



}
