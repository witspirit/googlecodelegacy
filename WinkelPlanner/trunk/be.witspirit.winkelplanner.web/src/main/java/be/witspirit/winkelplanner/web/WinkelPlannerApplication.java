package be.witspirit.winkelplanner.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

import be.witspirit.winkelplanner.HomePage;

public class WinkelPlannerApplication extends WebApplication
{
    @Override
    public Class<? extends Page> getHomePage() {
      return HomePage.class;
    }

    @Override
    protected void init() {
      super.init();
      getResourceSettings().setResourcePollFrequency(null);
      
      mountBookmarkablePage("HomePage.html", HomePage.class);
    }

    @Override
    protected ISessionStore newSessionStore() {
      return new HttpSessionStore(this);
    }
}
