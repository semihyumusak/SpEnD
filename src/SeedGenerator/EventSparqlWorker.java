package SeedGenerator;

import java.awt.AWTEvent;

/**
 * This class implement an user defined awt event which will be posted to the
 * AWT EventQueue. S.Kauss
 */
public class EventSparqlWorker extends AWTEvent {

    public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;

    private int analyzedSeedID;
    private String analyzedSeedUrl;
    
    EventSparqlWorker(Object target, int AnalyzedSeedId, String AnalyzedSeedUrl) {
        super(target, EVENT_ID);
        this.analyzedSeedID = analyzedSeedID;
        this.analyzedSeedUrl = analyzedSeedUrl;
    }

   
}                                               // MyAWTEvent

