package be.witspirit.winkelplanner.web.common.login;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;

import be.witspirit.winkelplanner.web.WinkelPlannerSession;

import com.google.appengine.api.users.User;

public class LoginStatusPanel extends Panel {
    
    private WinkelPlannerSession session;

    public LoginStatusPanel(String id) {
        super(id);
        session = WinkelPlannerSession.get();

        User user = session.getLoggedInUser();
        if (user == null) {
            add(new NotLoggedIn("status"));
        } else {
            add(new LoggedIn("status", user));
        }
    }

    public class NotLoggedIn extends Fragment {

        public NotLoggedIn(String id) {
            super(id, "notLoggedIn", LoginStatusPanel.this);
            ExternalLink login = new ExternalLink("login", session.getLoginUrl());
            add(login);
        }
    }

    public class LoggedIn extends Fragment {

        public LoggedIn(String id, User user) {
            super(id, "loggedIn", LoginStatusPanel.this);
            Label username = new Label("username", user.getNickname());
            add(username);
            ExternalLink logout = new ExternalLink("logout", session.getLogoutUrl());
            add(logout);
        }
    }
    
}
