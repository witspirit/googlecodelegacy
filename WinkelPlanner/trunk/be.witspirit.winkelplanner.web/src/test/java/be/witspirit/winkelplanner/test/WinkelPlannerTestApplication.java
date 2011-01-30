package be.witspirit.winkelplanner.test;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

import be.witspirit.winkelplanner.HomePage;
import be.witspirit.winkelplanner.web.WinkelPlannerSession;

import com.google.appengine.api.users.UserService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class WinkelPlannerTestApplication extends WebApplication {
    
    private Injector testInjector;
    
    public WinkelPlannerTestApplication(Module testModule) {
        this.testInjector = Guice.createInjector(testModule);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
    
    @Override
    protected void init() {
      addComponentInstantiationListener(new GuiceComponentInjector(this, testInjector));
    }
    
    @Override
    public Session newSession(Request request, Response response) {
        return new WinkelPlannerSession(request, testInjector.getInstance(UserService.class));
    }

}
