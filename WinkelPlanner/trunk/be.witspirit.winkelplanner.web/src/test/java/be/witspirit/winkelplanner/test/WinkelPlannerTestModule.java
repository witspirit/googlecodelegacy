package be.witspirit.winkelplanner.test;

import com.google.appengine.api.users.UserService;
import com.google.inject.AbstractModule;


public class WinkelPlannerTestModule extends AbstractModule {
    private boolean loggedIn;
    
    public WinkelPlannerTestModule(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    protected void configure() {
        bind(UserService.class).toInstance(new MockUserService(loggedIn));
    }

}
