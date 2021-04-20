package nio3.kbs.gsm_scan_server.DataBase.Sourses.ibase;

import lombok.Data;
import nio3.kbs.gsm_scan_server.DTO.SpeachAbbenentDateAmount;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Page;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.Speach;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.SpeachRepository;
import nio3.kbs.gsm_scan_server.DataBase.Sourses.SqlSort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Data
public class SpeachRepositoryIbaseImpl implements SpeachRepository {
private JdbcTemplate jdbcTemplate;
public SpeachRepositoryIbaseImpl(JdbcTemplate jbdc){
    jdbcTemplate=jbdc;
}
    private final  String getAll = "SELECT ST.S_TYPE, ST.S_INCKEY, ST.S_DATETIME, ST.S_NETWORK, ST.S_SYSNUMBER IMSI, ST.S_BASESTATION, ST.S_LAC, ST.S_CID, ST.S_SYSNUMBER2 TMSI, ST.S_SYSNUMBER3 IMEI, E.VAL_NAME Events, C.VAL_NAME EventType, S.VAL_NAME Status, SS.VAL_NAME Sign, ST.S_RCHANNEL, ST.S_DCHANNEL, ST.S_STANDARD, ST.S_DEVICEID, ST.S_EVENTCODE, ST.S_FREQUENCY, ST.S_DURATION " +
            "FROM SPR_SPEECH_TABLE ST " +
            "left join SPR_EVENT E ON E.VAL = ST.S_EVENT " +
            "left join SPR_CALLTYPE C ON C.VAL = ST.S_CALLTYPE " +
            "left join SPR_STATUS S ON S.VAL = ST.S_STATUS " +
            "left join SPR_SELSTATUS SS ON SS.VAL = ST.S_SELSTATUS WHERE ST.S_EVENT <> 10";

    private final String countSql="SELECT COUNT(*) FROM SPR_SPEECH_TABLE";


    private String ping= "SELECT 1 FROM SPR_SPEECH_TABLE";


    @Transactional(readOnly = true)
    @Override
    public Optional<Speach> findById(Long id) {

        return Optional.of(jdbcTemplate.query(addWhere(getAll,"ST.S_INCKEY = "+id),new SpeachRowMapperIbase()).get(0));
    }
    @Transactional(readOnly = true)
    @Override
    public List<Speach> getPage(Page p) {

        String sql=addLimitPagination(addOrderByDate(getAll), p.getStartRow(),p.getEndofPage());
       // System.out.println(sql);
        return  jdbcTemplate.query(sql,new SpeachIbaseRowExtractorIbase());
    }
    @Transactional(readOnly = true)
    @Override
    public List<Speach> getPageOrderById(Page p) {
        String sql=addLimitPagination(addOrderByIdDESC(getAll), p.getStartRow(),p.getEndofPage());
        return  jdbcTemplate.query(sql,new SpeachIbaseRowExtractorIbase());
    }


    /**
     * нужно проверить входят ли даты в диапазон
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Speach> getAllByPeriod(Date dateStart, Date dateEnd) {
      String d1=parseDate(dateStart);
      String d2=parseDate(dateEnd);
      String sql=addOrderByDate(addWhere(getAll," ST.S_DATETIME BETWEEN '"+d1+"' AND '"+d2+"' "));
      return  jdbcTemplate.query(sql,new SpeachIbaseRowExtractorIbase());
    }

    /**
     * @param p
     * @param dateStart
     * @param dateEnd
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Speach> getByPeriod(Page p, Date dateStart, Date dateEnd) {
        //p.setEndRow(getCountofRows());
        String d1=parseDate(dateStart);
        String d2=parseDate(dateEnd);
        String sql=addLimitPagination(addOrderByDate(addWhere(getAll," (ST.S_DATETIME BETWEEN '"+d1+"' AND '"+d2+"' )")),p.getStartRow(),p.getEndofPage());

        //System.out.println(sql);
        return  jdbcTemplate.query(sql,new SpeachIbaseRowExtractorIbase());
    }
    @Transactional(readOnly = true)
    @Override
    public List<Speach> findAll() {
        return  jdbcTemplate.query(addOrderByDate(getAll),new SpeachIbaseRowExtractorIbase());
    }
    @Transactional(readOnly = true)
    @Override
    public Long getCountofRows()
    {
        return jdbcTemplate.queryForObject(countSql, Long.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Speach> findAllByIdMore(Page p, Long id) {
        String sql=addLimitPagination(addOrderByIdASC(addWhere(getAll, "ST.S_INCKEY >"+id)), p.getStartRow(),p.getEndofPage());
        // System.out.println(sql);
        return  jdbcTemplate.query(sql,new SpeachIbaseRowExtractorIbase());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Speach> getPageLessId(Page p, Long id) {
        String sql=addLimitPagination(addOrderByIdDESC(addWhere(getAll, "ST.S_INCKEY <"+id)), p.getStartRow(),p.getEndofPage());
        // System.out.println(sql);
        return  jdbcTemplate.query(sql,new SpeachIbaseRowExtractorIbase());
    }

    /**
     * Найти последний id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Long findLastId() {
        Long id=jdbcTemplate.queryForObject(addLimit(addOrderByIdDESC(getAll), 1), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                Long id= resultSet.getLong("S_INCKEY");
                return id;
            }
        });
        if (id==null) id=-1l;
        return id;
    }

    @Override
    public void ping() {

        jdbcTemplate.execute(ping);
        return;
    }

    private  String addLimit(String sql, int limit) {
        return sql + " ROWS " + limit;
    }
    private  String addLimitPagination(String sql, Integer start,Integer end) {
        return sql + " ROWS " + start+" TO "+end;
    }

    private  String addWhere(String sql, String where) {
        return sql + " AND " + where;
    }

    public  String filter(SqlSort sort, int count) {
        switch (sort) {
            case All: return addLimit(this.getAll, count);
            case Registrations: return addLimit(addWhere(getAll, " ST.S_EVENT = 4 "), count);
            case SMS: return addLimit(addWhere(getAll, " ST.S_TYPE = 1 "), count);
            case Colls: return addLimit(addWhere(getAll, " ST.S_TYPE = 0 "), count);
            default: return addLimit(getAll, count);
        }
    }
    private String parseDate(Date d){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(d);
    }
    private String addOrderByDate(String sql)
    {
        return sql+" order by  ST.S_DATETIME DESC";
    }
    private String addOrderByIdDESC(String sql) {return sql+" order by  ST.S_INCKEY DESC ";}
    private String addOrderByIdASC(String sql) {return sql+" order by  ST.S_INCKEY ";}
}
