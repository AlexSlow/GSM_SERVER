package nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase;

import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.rowset.JdbcRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SpeachRowMapperIbase implements RowMapper<Speach> {
    @Override
    public Speach mapRow(ResultSet rs, int i) throws SQLException {
        Speach speach=new Speach();
        speach.setId(rs.getLong("S_INCKEY"));
        speach.setDate(rs.getDate("S_DATETIME"));
        speach.setOperator(rs.getString("S_NETWORK"));
        speach.setIMSI(rs.getString("IMSI"));
        speach.setStation(rs.getString("S_BASESTATION"));
        speach.setLAC(rs.getString("S_LAC"));
        speach.setCID(rs.getString("S_CID"));
        speach.setIMSI(rs.getString("TMSI"));
        speach.setIMEI(rs.getString("IMEI"));
        speach.setEvent(rs.getString("Events"));
        speach.setEventType(rs.getString("EventType"));
        speach.setStatus(rs.getString("Status"));
        speach.setSign(rs.getString("Sign"));
        speach.setS_RCHANNEL(rs.getString("S_RCHANNEL"));
        speach.setS_DCHANNEL(rs.getString("S_DCHANNEL"));
        speach.setS_STANDARD(rs.getString("S_STANDARD"));
        speach.setS_DEVICEID(rs.getString("S_DEVICEID"));
        speach.setS_EVENTCODE(rs.getString("S_EVENTCODE"));
        speach.setS_FREQUENCY(rs.getString("S_FREQUENCY"));
        speach.setS_DURATION(rs.getString("S_DURATION"));
        speach.setS_INCKEY(rs.getString("S_INCKEY"));
        speach.setS_TYPE(rs.getString("S_TYPE"));
        return speach;
    }
}
