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

import com.google.appengine.api.users.UserService;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class WinkelPlannerApplication extends WebApplication {     
    
    private Injector injector;
    
    @Override
    public Class<? extends Page> getHomePage() {
      return HomePage.class;
    }

    @Override
    protected void init() {
      super.init();
      getResourceSettings().setResourcePollFrequency(null);
      this.injector = Guice.createInjector(new WinkelPlannerModule());
      addComponentInstantiationListener(new GuiceComponentInjector(this, injector));
      
      mountBookmarkablePage("HomePage.html", HomePage.class);
    }
    
    public void inject(Object target) {
        injector.injectMembers(target);
    }

    @Override
    protected ISessionStore newSessionStore() {
      return new HttpSessionStore(this);
    }
    
    @Override
    public Session newSession(Request request, Response response) {
        return new WinkelPlannerSession(request, injector.getInstance(UserService.class));
    }
}
