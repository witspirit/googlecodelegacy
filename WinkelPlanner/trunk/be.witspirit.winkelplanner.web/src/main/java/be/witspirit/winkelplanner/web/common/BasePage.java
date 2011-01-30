package be.witspirit.winkelplanner.web.common;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;

import be.witspirit.winkelplanner.web.common.login.LoginStatusPanel;

public class BasePage extends WebPage {
    
    public BasePage() {
        add(new EmptyPanel("sidebar"));
        add(new LoginStatusPanel("login-status"));
    }

}
