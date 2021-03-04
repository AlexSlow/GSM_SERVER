package nio3.kbs.gsm_scan_server.DtoRouter;

import nio3.kbs.gsm_scan_server.DTO.StantionDto;

import java.util.List;

public class StantionDTORouter extends DtoRouter <List<StantionDto>> {

    @Override
    public void route(List< StantionDto> data) {
        if (this.getNextRouter()!=null) this.getNextRouter().route(data);
    }
}
