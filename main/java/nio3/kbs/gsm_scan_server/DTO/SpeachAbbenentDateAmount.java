package nio3.kbs.gsm_scan_server.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SpeachAbbenentDateAmount {

    private List<Map<Date,Integer>> dateAmount=new ArrayList<>();
}
