package SeedGenerator;

import java.awt.AWTEvent;

/**
 * This class implement an user defined awt event which will be posted to the
 * AWT EventQueue. S.Kauss
 */
public class EventSearchWorker extends AWTEvent {

    public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;
    private String str;
    private int percent;
    private String message;
    
    private int analyzedSeedID;
    private String analyzedSeedUrl;
    private Object[] rowValues;
    
    

    EventSearchWorker(Object target, String str, int percent, String Message) {
        super(target, EVENT_ID);
        this.str = str;
        // this.percent = percent;
        this.message = Message;
    }
    EventSearchWorker(Object target, Object[] rowValues, int percent,String Message) {
        super(target, EVENT_ID);
      this.percent = percent;
        this.message = Message;
        this.rowValues= rowValues;
    }

    EventSearchWorker(Object target, int percent) {
        super(target, EVENT_ID);
        this.percent = percent;
    }
    EventSearchWorker(Object target, int AnalyzedSeedId, String AnalyzedSeedUrl) {
        super(target, EVENT_ID);
        this.analyzedSeedID = analyzedSeedID;
        this.analyzedSeedUrl = analyzedSeedUrl;
    }

    public Object[] getRowValues() {
        return (rowValues);
    }
    public String getStr() {
        return (str);
    }

    public int getPercent() {
        return (percent);
    }

    public String getMessage() {
        return (message);
    }

    public boolean hasMessage() {
       try
       {
        if (message == null) {
            return false;
        }
        if (message.equals("")) {
            return false;
        } else {
            return true;
        }
       }
       catch(Exception ex)
       {
           return false;
       }
    }
}                                               // MyAWTEvent

