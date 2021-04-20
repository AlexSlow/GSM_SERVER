package nio3.kbs.gsm_scan_server;

import lombok.extern.slf4j.Slf4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.*;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase.SpeachRepositoryIbaseImpl;
import nio3.kbs.gsm_scan_server.Service.StantionProcessingService;
import nio3.kbs.gsm_scan_server.clients.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.List;
@EnableScheduling
@EnableCaching
@SpringBootApplication
@Slf4j
public class GsmScanServerApplication {
    @Autowired Settings settings;
    @Autowired
    @Qualifier("StationMonitoring")
  private StantionProcessingService stantionProcessingService;



	public static void main(String[] args) {
		SpringApplication.run(GsmScanServerApplication.class, args);
		log.info("Сервер запущен");
	}

    @PostConstruct
    public void init() throws InterruptedException {

        //Десериализация станций
        JdbcConnection jdbcConnection =new JdbcConnection();

/*

        Stantion stantion=new Stantion();
        stantion.setUsername("sysdba");
        stantion.setPassword("masterkey");
        stantion.setTypeConnection(TypeConnection.InterBase);
        stantion.setFile("D:/interbase/NEW5_3.ibs");
        stantion.setHost("localhost");
        stantion.setName("123 Vas");


        Stantion stantion1=new Stantion();
        stantion1.setUsername("sysdba");
        stantion1.setPassword("masterkey");
        stantion1.setTypeConnection(TypeConnection.InterBase);
        stantion1.setFile("D:/interbase/NEW5_3.ibs");
        stantion1.setHost("localhost");
        stantion1.setName("456 ");


       settings.clear();
       settings.add(stantion);
       settings.add(stantion1);


        SettingsSerializator settingsSerializator=new SettingsSerializator();
        settingsSerializator.serialize(settings);
*/

/*
        System.out.println(settings.getStantionList().get(0));
        JdbcTemplate jdbcTemplate= jdbcConnection.getJdbc(settings.getStantionList().get(0));
        SpeachRepository speachRepository=new SpeachRepositoryIbaseImpl(jdbcTemplate);

        System.out.println(speachRepository.getAbbonentForDateAmmount());
       // print(speachRepository.getPage(new Page(100)));
*/


        /*
        StantionScaningService stantionScaningService=new StantionScaningService();
        stantionScaningService.setStantion(settings.getStantionList().get(0));
        stantionScaningService.setConnectionFactory(new ConnectionFactory());
        stantionScaningService.start();

        stantionScaningService.stop();
*/

        //Тест роутов и многопоточности

      //  stationProcessingService.start();

    }

    /**
     * Для тестов
     * @param speachList
     */
	void print(List<Speach> speachList)
    {
        speachList.forEach(s->{
            System.out.println(s);
        });
    }

}
