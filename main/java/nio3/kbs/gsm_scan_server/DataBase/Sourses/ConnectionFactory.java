package nio3.kbs.gsm_scan_server.DataBase.Sourses;



import nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase.SpeachRepositoryIbaseImpl;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFactory {
    @Cacheable(value ="Repository")
    public SpeachRepository getRepository( Stantion stantion){
        JdbcConnection jdbcConnection=new JdbcConnection();
        JdbcTemplate jdbcTemplate=jdbcConnection.getJdbc(stantion);
        return getRepository(stantion.getTypeConnection(),jdbcTemplate);

    }

    private   SpeachRepository getRepository(TypeConnection typeConnection,JdbcTemplate jdbcTemplate){
        switch (typeConnection)
        {
            case InterBase:
                return new SpeachRepositoryIbaseImpl(jdbcTemplate);
            default:throw new RuntimeException("Отсутствует новый тип данных");
        }
    }
}
