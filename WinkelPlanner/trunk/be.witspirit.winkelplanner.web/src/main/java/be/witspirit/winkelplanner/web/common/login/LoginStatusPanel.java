package be.witspirit.winkelplanner.web.common.login;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebRequestCycle;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;

public class LoginStatusPanel extends Panel {

    @Inject
    private UserService userService;

    public LoginStatusPanel(String id) {
        super(id);

        User user = userService.getCurrentUser();
        if (user == null) {
            add(new NotLoggedIn("status"));
        } else {
            add(new LoggedIn("status", user));
        }
    }

    private String destinationUrl() {
        String targetUrl = ((WebRequestCycle) WebRequestCycle.get()).getWebRequest().getURL();
        return targetUrl;
    }

    public class NotLoggedIn extends Fragment {

        public NotLoggedIn(String id) {
            super(id, "notLoggedIn", LoginStatusPanel.this);
            ExternalLink login = new ExternalLink("login", userService.createLoginURL(destinationUrl()));
            add(login);
        }
    }

    public class LoggedIn extends Fragment {

        public LoggedIn(String id, User user) {
            super(id, "loggedIn", LoginStatusPanel.this);
            Label username = new Label("username", user.getNickname());
            add(username);
            ExternalLink logout = new ExternalLink("logout", userService.createLogoutURL(destinationUrl()));
            add(logout);
        }
    }
    
}
