package nio3.kbs.gsm_scan_server.DataBase.Sourses;

import nio3.kbs.gsm_scan_server.clients.Stantion;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class JdbcConnection {
    //Был статический
    private   BasicDataSource basicDataSource =new BasicDataSource();

    public JdbcTemplate getJdbc(Stantion stantion) {
        basicDataSource.setDriverClassName(getDriver(stantion.getTypeConnection()));
        //dataSourceBuilder.url("jdbc:mysql://localhost/test?serverTimezone=Europe/Moscow&useSSL=false&allowPublicKeyRetrieval=true");
        basicDataSource.setUrl(getHost(stantion.getTypeConnection(),stantion.getHost(),stantion.getFile()));
        basicDataSource.setUsername(stantion.getUsername());
        basicDataSource.setPassword(stantion.getPassword());
        addConectionpropereties(stantion.getTypeConnection());
        return  new JdbcTemplate(basicDataSource);
    }

    private void addConectionpropereties(TypeConnection typeConnection){
        switch (typeConnection){
            case InterBase:
                basicDataSource.addConnectionProperty("encoding", "WIN1251");
                break;
        }
    }

    private String getHost(TypeConnection typeConnection,String host,String file)
    {
        switch (typeConnection)
        {
            case InterBase:
                return String.format("jdbc:firebirdsql://%s:3050/%s",host, file);
            case Postgress:
                return String.format("jdbc:postgresql://%s:5432/%s",host, file);
            case MySql:
                return host;
            default: return host;

        }
    }
    private String getDriver(TypeConnection typeConnection)
    {
        switch (typeConnection)
        {
            case InterBase:
                return "org.firebirdsql.jdbc.FBDriver";
            case Postgress:
                return "";
            case MySql:
                return "com.mysql.cj.jdbc.Driver";
            default:
                return "";

        }
    }
}
