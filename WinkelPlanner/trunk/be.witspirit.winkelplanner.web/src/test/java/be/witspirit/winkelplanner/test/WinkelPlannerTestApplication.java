package be.witspirit.winkelplanner.test;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

import com.google.inject.Module;

import be.witspirit.winkelplanner.HomePage;

public class WinkelPlannerTestApplication extends WebApplication {
    
    private Module testModule;
    
    public WinkelPlannerTestApplication(Module testModule) {
        this.testModule = testModule;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
    
    @Override
    protected void init() {
      addComponentInstantiationListener(new GuiceComponentInjector(this, testModule));
    }

}
