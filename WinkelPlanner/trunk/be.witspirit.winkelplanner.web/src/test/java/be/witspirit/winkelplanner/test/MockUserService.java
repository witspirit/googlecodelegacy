package be.witspirit.winkelplanner.test;

import java.util.Set;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

public class MockUserService implements UserService {

    private static final String LOGOUT_URL = "logoutUrl";
    private static final String LOGIN_URL = "loginUrl";
    
    private User user;
    
    public MockUserService(boolean loggedIn) {
        if (loggedIn) {
            user = new User("testuser@example.com", "testDomain", "testUserId");
        } else {
            user = null;
        }
    }

    @Override
    public String createLoginURL(String arg0) {
        return LOGIN_URL;
    }

    @Override
    public String createLoginURL(String arg0, String arg1) {
        return LOGIN_URL;
    }

    @Override
    public String createLoginURL(String arg0, String arg1, String arg2,
            Set<String> arg3) {
        return LOGIN_URL;
    }

    @Override
    public String createLogoutURL(String arg0) {
        return LOGOUT_URL;
    }

    @Override
    public String createLogoutURL(String arg0, String arg1) {
        return LOGOUT_URL;
    }

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public boolean isUserAdmin() {
        return false;
    }

    @Override
    public boolean isUserLoggedIn() {
        return user != null;
    }

}
