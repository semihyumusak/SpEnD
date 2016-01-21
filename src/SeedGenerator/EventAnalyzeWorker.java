package SeedGenerator;

import java.awt.AWTEvent;

/**
 * This class implement an user defined awt event which will be posted to the
 * AWT EventQueue. S.Kauss
 */
public class EventAnalyzeWorker extends AWTEvent {

    public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;
    private int percent;
    private String message;
    
    private int analyzedSeedID;
    private String analyzedSeedUrl;
    
    EventAnalyzeWorker(Object target, String str,int percent, String Message) {
        super(target, EVENT_ID);
    
        // this.percent = percent;
        this.message = Message;
    }

    EventAnalyzeWorker(Object target, int percent) {
        super(target, EVENT_ID);
        this.percent = percent;
    }
    EventAnalyzeWorker(Object target, int AnalyzedSeedId, String AnalyzedSeedUrl) {
        super(target, EVENT_ID);
        this.analyzedSeedID = analyzedSeedID;
        this.analyzedSeedUrl = analyzedSeedUrl;
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

