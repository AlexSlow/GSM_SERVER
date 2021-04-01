package nio3.kbs.gsm_scan_server.DTO;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.clients.Stantion;

import java.util.List;
import java.util.Objects;

@Data
public class StantionSpeachDTO {
    private StantionDto stantionDto;
    private List<Speach> speachList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StantionSpeachDTO)) return false;
        StantionSpeachDTO that = (StantionSpeachDTO) o;
        return getStantionDto().equals(that.getStantionDto()) &&
                getSpeachList().equals(that.getSpeachList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStantionDto(), getSpeachList());
    }

    public StantionSpeachDTO() {
    }

}
