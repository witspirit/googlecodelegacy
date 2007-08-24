/**
 * @author wItspirit
 * 16-okt-2005
 * AuthorizationHandler.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.net.URL;

/**
 * This interface has to be implemented by a UI component that will facilitate
 * the URL authorization request.
 */
public interface AuthorizationHandler
{
    /**
     * This method should redirect the user to the URL that is passed in.
     * AFTER the user has visited the URL and authorized the application 
     * this method should return.
     * @param url The URL to authorize this application
     * @return true if the user wants to proceed for login
     *         false if the user wants to proceed without logging in
     */
    public boolean requestAuthorization(URL url);

}
