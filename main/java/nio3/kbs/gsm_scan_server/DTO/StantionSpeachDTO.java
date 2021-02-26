package nio3.kbs.gsm_scan_server.DTO;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import java.util.Objects;

@Data
public class StantionSpeachDTO {
    private Stantion stantion;
    private Speach speach;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StantionSpeachDTO)) return false;
        StantionSpeachDTO that = (StantionSpeachDTO) o;
        return getStantion().equals(that.getStantion()) &&
                getSpeach().equals(that.getSpeach());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStantion(), getSpeach());
    }
}
