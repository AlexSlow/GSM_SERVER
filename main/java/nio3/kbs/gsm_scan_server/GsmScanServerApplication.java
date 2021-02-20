package nio3.kbs.gsm_scan_server;

import lombok.extern.slf4j.Slf4j;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.*;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase.SpeachRepositoryIbaseImpl;
import nio3.kbs.gsm_scan_server.Service.StationProcessingService;
import nio3.kbs.gsm_scan_server.clients.Settings;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import nio3.kbs.gsm_scan_server.clients.stationSerializator.Serializator;
import nio3.kbs.gsm_scan_server.clients.stationSerializator.SettingsSerializator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Slf4j
public class GsmScanServerApplication {
    @Autowired Settings settings;
    @Autowired
  private  StationProcessingService stationProcessingService;
	public static void main(String[] args) {
		SpringApplication.run(GsmScanServerApplication.class, args);
		log.info("Сервер запущен");
	}

    @PostConstruct
    public void init() {

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
        settings.add(stantion);
        SettingsSerializator settingsSerializator=new SettingsSerializator();
        settingsSerializator.serialize(settings);
*/

        JdbcTemplate jdbcTemplate= jdbcConnection.getJdbc(settings.getStantionList().get(0));
        SpeachRepository speachRepository=new SpeachRepositoryIbaseImpl(jdbcTemplate);

        //System.out.println(speachRepository.findAll());
        //System.out.println(speachRepository.findById(4l).get());
        // print(speachRepository.getAllByPeriod(new Date(10000),new Date()));

        /*

        try {
            Page p=new Page(10);
            print(speachRepository.getPage(p));
            p.next();
            System.out.println("---------------------------------------------------");
            Thread.sleep(2000);

            print(speachRepository.getPage(p));
            p.next();
            System.out.println("---------------------------------------------------");
            Thread.sleep(2000);


            print(speachRepository.getPage(p));
            p.next();
            System.out.println("---------------------------------------------------");
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        /*
        Page p=new Page(10);
        print( speachRepository.getPageOrderById(p));
        p.next();

        print( speachRepository.getPageOrderById(p));
        p.next();

        print( speachRepository.getPageOrderById(p));
        p.next();

        */
        // print(speachRepository.findAllByIdMore(new Page(50),2000l));

       // System.out.println(speachRepository.findLastId());

        stationProcessingService.start();

    }

	void print(List<Speach> speachList)
    {
        speachList.forEach(s->{
            System.out.println(s);
        });
    }

}
