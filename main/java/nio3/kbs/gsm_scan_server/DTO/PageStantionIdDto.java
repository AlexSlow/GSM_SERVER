package nio3.kbs.gsm_scan_server.DTO;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Page;

@Data
public class PageStantionIdDto {
    Page page;
    Integer StantionId;

    public PageStantionIdDto(Page page, Integer stantionId) {
        this.page = page;
        StantionId = stantionId;
    }
}
