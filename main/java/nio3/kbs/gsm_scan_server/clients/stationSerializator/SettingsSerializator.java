package nio3.kbs.gsm_scan_server.clients.stationSerializator;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Data
@Slf4j
public class SettingsSerializator implements Serializator<Settings> {
private final String file_name="stantions.json";
private Path path;
    @Override
    public void serialize(Settings settings) {
        log.debug("Начало сериализации станций");
        try {
            initFile();
            SaveSettings(settings);

        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Ошибка создания файла "+file_name);
        }
    }

    @Override
    public Settings deserialize() {

        log.debug("Начало десериализации станций");
        path=Paths.get(file_name);
            if (Files.exists(path))
            return new Gson().fromJson(GetSettingsFile(), Settings.class);
           else {
                log.warn("ошибка. Нет файла "+file_name);
            }

        return new Settings();
    }
    private void initFile() throws IOException {
        path= Paths.get(file_name);
        if (Files.exists(path)) Files.delete(path);
        Files.createFile(path);
    }
    private  String GetSettingsFile() {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "{}";
        }
    }

    private   void SaveSettings(Settings settings) {
        ArrayList<String> jsonString = new ArrayList<String>();
       // settings.setActive(false);
        jsonString.add(new Gson().toJson(settings));
        try {

            Files.write(path, jsonString, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
