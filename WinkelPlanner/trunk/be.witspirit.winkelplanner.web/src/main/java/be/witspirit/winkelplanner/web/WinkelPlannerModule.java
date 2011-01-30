package be.witspirit.winkelplanner.web;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;

public class WinkelPlannerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).toInstance(UserServiceFactory.getUserService());
    }

}
