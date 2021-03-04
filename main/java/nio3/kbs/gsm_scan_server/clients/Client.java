package nio3.kbs.gsm_scan_server.clients;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;


@Data
public class Client implements Serializable {
    public Client(){}
    public Client(String UUID){ this.UUID=UUID;}
    private String UUID;
    private String name;
    private Integer lon;
    private Integer lat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getUUID().equals(client.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID());
    }
    public void setInfo(Client client)
    {
        this.setName(client.getName());
        this.setLat(client.getLat());
        this.setLon(client.getLon());
    }

    @Override
    public String toString() {
        return "Client{" +
                "UUID='" + UUID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
