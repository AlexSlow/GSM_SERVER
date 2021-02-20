package nio3.kbs.gsm_scan_server.DataBase.Sourses;


import com.sun.istack.internal.NotNull;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase.SpeachRepositoryIbaseImpl;
import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFactory {
    public SpeachRepository getRepository(@NotNull Stantion stantion){
        JdbcConnection jdbcConnection=new JdbcConnection();
        JdbcTemplate jdbcTemplate=jdbcConnection.getJdbc(stantion);
        return getRepository(stantion.getTypeConnection(),jdbcTemplate);

    }


    private   SpeachRepository getRepository(TypeConnection typeConnection,JdbcTemplate jdbcTemplate){
        switch (typeConnection)
        {
            case InterBase:
                return new SpeachRepositoryIbaseImpl(jdbcTemplate);
            case MySql:
            case Postgress:
                return null;
            default:return null;
        }
    }
}