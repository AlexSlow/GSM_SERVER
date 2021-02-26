package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.TypeConnection;

import java.math.BigInteger;
import java.util.Objects;

@Data
public class Stantion {
    private Integer id;
    private String name;
   private String username;
   private String password;
   private String host;
   private String file;
   private TypeConnection typeConnection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stantion)) return false;
        Stantion stantion = (Stantion) o;
        return getId().equals(stantion.getId()) &&
                Objects.equals(getName(), stantion.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
