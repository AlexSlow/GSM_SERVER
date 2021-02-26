package nio3.kbs.gsm_scan_server.websocket;

import java.security.Principal;

public class StompPrincipal implements Principal {
    String name;

  public  StompPrincipal(String name) {
        this.name = name;
    }

    @Override
   public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
