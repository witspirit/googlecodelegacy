package be.witspirit.winkelplanner.web;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

public class WinkelPlannerSession extends WebSession {
    
    public static WinkelPlannerSession get() {
        return WinkelPlannerSession.class.cast(WebSession.get());
    }

    public WinkelPlannerSession(Request request) {
        super(request);
    }

}
