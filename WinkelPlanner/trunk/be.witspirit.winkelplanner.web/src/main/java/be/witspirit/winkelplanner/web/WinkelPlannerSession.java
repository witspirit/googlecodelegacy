package be.witspirit.winkelplanner.web;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

public class WinkelPlannerSession extends WebSession {
    
    private UserService userService;
    
    public static WinkelPlannerSession get() {
        return WinkelPlannerSession.class.cast(WebSession.get());
    }

    public WinkelPlannerSession(Request request, UserService userService) {
        super(request);
        this.userService = userService;
    }
    
    public User getLoggedInUser() {
        return userService.getCurrentUser();
    }
    
    private String destinationUrl() {
        String targetUrl = ((WebRequestCycle) WebRequestCycle.get()).getWebRequest().getURL();
        return targetUrl;
    }
    
    public String getLoginUrl() {
        return userService.createLoginURL(destinationUrl());
    }
    
    public String getLogoutUrl() {
        return userService.createLogoutURL(destinationUrl());
    }

}
