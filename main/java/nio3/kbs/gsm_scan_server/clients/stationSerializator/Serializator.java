package nio3.kbs.gsm_scan_server.clients.stationSerializator;

import nio3.kbs.gsm_scan_server.clients.Stantion;

import java.util.List;

public interface Serializator<T> {
    void serialize(T t);
    T deserialize();
}
