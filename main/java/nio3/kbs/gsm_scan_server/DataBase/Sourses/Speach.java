package nio3.kbs.gsm_scan_server.DataBase.Sourses;

import lombok.Data;

import java.util.Date;

@Data
public class Speach {
    private Long id;
    private Date date;
    private String Operator;
    private String IMSI;
    private String Station;
    private String LAC;
    private String CID;
    private String TMSI;
    private String IMEI;
    private String Event;
    private String EventType;
    private String Status;
    private String Sign;
    private String S_RCHANNEL;
    private String S_DCHANNEL;
    private String S_STANDARD;
    private String S_DEVICEID;
    private String S_EVENTCODE;
    private String S_FREQUENCY;
    private String S_DURATION;
    private String S_INCKEY;
    private String S_TYPE;
}
