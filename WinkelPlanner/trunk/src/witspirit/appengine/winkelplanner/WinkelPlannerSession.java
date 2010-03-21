package witspirit.appengine.winkelplanner;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

public class WinkelPlannerSession extends WebSession {
    
    public static WinkelPlannerSession get() {
        return (WinkelPlannerSession) WebSession.get();
    }

    public WinkelPlannerSession(Request request) {
        super(request);
    }

}
