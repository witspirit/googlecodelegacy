package witspirit.appengine.winkelplanner.web.common;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class BasePage extends WebPage {
    
    public BasePage() {
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();

	String targetUrl = getWebRequestCycle().getWebRequest().getURL();
	ExternalLink login = new ExternalLink("login", userService.createLoginURL(targetUrl));
	add(login);

	WebMarkupContainer userInfo = new WebMarkupContainer("userInfo");
	add(userInfo);
	Label username = new Label("username", user != null ? user.getNickname() : "-");
	userInfo.add(username);
	ExternalLink logout = new ExternalLink("logout", userService.createLogoutURL(targetUrl));
	userInfo.add(logout);

	if (user == null) {
	    userInfo.setVisible(false);
	} else {
	    login.setVisible(false);
	}
	
	
    }
    
    protected User getUser() {
	UserService userService = UserServiceFactory.getUserService();
	return userService.getCurrentUser();
    }

}
