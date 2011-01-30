package be.witspirit.winkelplanner.web;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.guice.GuiceComponentInjector;
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
      addComponentInstantiationListener(new GuiceComponentInjector(this, new WinkelPlannerModule()));
      
      mountBookmarkablePage("HomePage.html", HomePage.class);
    }

    @Override
    protected ISessionStore newSessionStore() {
      return new HttpSessionStore(this);
    }
    
    @Override
    public Session newSession(Request request, Response response) {
        return new WinkelPlannerSession(request);
    }
}
