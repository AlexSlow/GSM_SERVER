package nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase;

import nio3.kbs.gsm_scan_server.DTO.SpeachAbbenentDateAmount;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DateAmountExtractorIbase implements ResultSetExtractor<SpeachAbbenentDateAmount> {
@Override
public SpeachAbbenentDateAmount extractData(ResultSet rs) throws SQLException, DataAccessException {
      SpeachAbbenentDateAmount speachAbbenentDateAmount=new SpeachAbbenentDateAmount();
        while (rs.next()) {
            Map<Date,Integer> map=new HashMap<>();
        map.put(rs.getDate("S_DATETIME"),rs.getInt("Amount"));
        speachAbbenentDateAmount.getDateAmount().add(map);
        }
        return  speachAbbenentDateAmount;
        }
}
