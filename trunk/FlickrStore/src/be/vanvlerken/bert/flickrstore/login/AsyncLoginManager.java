/**
 * @author wItspirit
 * 28-okt-2005
 * AsyncLoginManager.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AsyncLoginManager {
    private AuthenticatedExecutor executor;
    private SyncLoginManager loginManager;

    private class Login implements Callable<Object> {
	private AuthorizationHandler authHandler;

	public Login(AuthorizationHandler authHandler) {
	    this.authHandler = authHandler;
	}

	public Object call() throws Exception {
	    loginManager.login(authHandler);
	    return null;
	}
    }

    private class GetUser implements Callable<FlickrUser> {
	public FlickrUser call() throws Exception {
	    return loginManager.getUser();
	}
    }

    private class Logout implements Callable<Object> {
	public Object call() throws Exception {
	    loginManager.logout();
	    return null;
	}
    }

    public AsyncLoginManager(AuthenticatedExecutor executor, SyncLoginManager loginManager) {
	this.executor = executor;
	this.loginManager = loginManager;
    }

    public Future<Object> login(AuthorizationHandler authHandler) {
	return executor.submit(new Login(authHandler));
    }

    public Future<Object> logout() {
	Future<Object> result = executor.submit(new Logout());
	executor.resetExecutor();
	return result;
    }

    public Future<FlickrUser> getUser() {
	return executor.submit(new GetUser());
    }
}
