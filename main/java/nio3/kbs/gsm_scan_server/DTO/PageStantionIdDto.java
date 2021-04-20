package nio3.kbs.gsm_scan_server.DTO;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Page;

@Data
public class PageStantionIdDto {
    Page page;
    Long StantionId;

    /**
     * Используется только в повторном обращении. Просто данный паект используется в 2-х
     * операциях
     */
    Long SpeachId;

    public PageStantionIdDto(Page page, Long stantionId) {
        this.page = page;
        StantionId = stantionId;
    }
}
