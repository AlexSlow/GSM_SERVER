package nio3.kbs.gsm_scan_server.DTO;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;

@Data
public class SpeachAlgoritmInfoDTO {
    private StantionSpeachDTO speach;
    private Integer algoritmKod;
    private String comment;
}
