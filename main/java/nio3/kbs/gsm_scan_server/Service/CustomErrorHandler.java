package nio3.kbs.gsm_scan_server.Service;

import java.util.concurrent.ScheduledFuture;

/**
 * При ошибке в потоке у станции
 */
public interface CustomErrorHandler {
    void onError(Exception e);
}
