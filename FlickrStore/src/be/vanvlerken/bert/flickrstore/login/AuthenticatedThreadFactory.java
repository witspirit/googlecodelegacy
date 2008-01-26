/**
 * @author wItspirit
 * 3-nov-2005
 * AuthenticatedThreadFactory.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;

/**
 * Delivers threads that are logged in to Flickr
 */
public class AuthenticatedThreadFactory implements ThreadFactory {
    private ThreadFactory defaultThreadFactory;
    private SyncLoginManager loginManager;
    private AuthorizationHandler authHandler;

    public AuthenticatedThreadFactory(SyncLoginManager loginManager, AuthorizationHandler authHandler) {
	defaultThreadFactory = Executors.defaultThreadFactory();
	this.loginManager = loginManager;
	this.authHandler = authHandler;
    }

    private class AuthenticatedRunner implements Runnable {
	private Runnable runner;

	public AuthenticatedRunner(Runnable runner) {
	    this.runner = runner;
	}

	public void run() {
	    try {
		loginManager.login(authHandler);
		runner.run();
	    } catch (FlickrAuthorizationRequiredException e) {
		e.printStackTrace();
	    } catch (FlickrCommunicationException e) {
		e.printStackTrace();
	    }
	}
    }

    public Thread newThread(Runnable runner) {
	Runnable authenticatedRunner = new AuthenticatedRunner(runner);
	return defaultThreadFactory.newThread(authenticatedRunner);
    }
}
