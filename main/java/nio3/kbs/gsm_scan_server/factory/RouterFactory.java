package nio3.kbs.gsm_scan_server.factory;

import nio3.kbs.gsm_scan_server.DtoRouter.AlgoritmRouter;
import nio3.kbs.gsm_scan_server.DtoRouter.ClientRouter;
import nio3.kbs.gsm_scan_server.DtoRouter.DtoRouter;
import nio3.kbs.gsm_scan_server.algoritm.AlgoritmManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouterFactory {
    @Autowired private AlgoritmManager algoritmManager;

    public DtoRouter toClientAndAlgoritmRoute()
    {
        ClientRouter clientRouter=new ClientRouter();
        clientRouter.setNextRouter(toAlgoritm());
        return clientRouter;
    }
    public  DtoRouter toAlgoritm(){
        AlgoritmRouter algoritmRouter=new AlgoritmRouter();
        algoritmRouter.setAlgoritmManager(algoritmManager);
        return algoritmRouter;
    }
}
