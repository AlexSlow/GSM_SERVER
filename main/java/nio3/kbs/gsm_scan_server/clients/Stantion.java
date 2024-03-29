package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.TypeConnection;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Data
public class Stantion implements Serializable {
    private Long id;
    private String name;
   private String username;
   private String password;
   private String host;
   private String file;
   private TypeConnection typeConnection;
   private transient StantionStatus status=StantionStatus.notAvailable;
   private Double coord_X;
   private Double coord_Y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stantion)) return false;
        Stantion stantion = (Stantion) o;
        return getId().equals(stantion.getId()) &&
                getName().equals(stantion.getName()) &&
                getHost().equals(stantion.getHost()) &&
                getFile().equals(stantion.getFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getHost(), getFile());
    }
}
