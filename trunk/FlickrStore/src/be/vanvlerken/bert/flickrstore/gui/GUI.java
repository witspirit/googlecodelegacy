/**
 * @author wItspirit
 * 19-okt-2005
 * GUI.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import be.vanvlerken.bert.components.gui.applicationwindow.ShutdownListener;
import be.vanvlerken.bert.flickrstore.AsyncFlickrFactory;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

public class GUI implements Runnable, ShutdownListener {
    public void run() {
	FlickrUser user = null;

	try {
	    AuthorizationProcess authProcess = new AuthorizationProcess();

	    AsyncFlickrFactory flickrFactory = new AsyncFlickrFactory(authProcess);

	    Future<FlickrUser> flickrUserFuture = flickrFactory.getLoginManager().getUser();
	    user = flickrUserFuture.get();

	    System.out.println("Logged in as " + user.getUsername());

	    ActionFactory actionFactory = new ActionFactory(flickrFactory, user);

	    MainWindow mainWindow = new MainWindow(actionFactory);
	    mainWindow.setShutdownListener(this);
	    mainWindow.setVisible(true);	    
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    performShutdown();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    performShutdown();
	}
    }

    public void performShutdown() {
	System.out.println("FlickrStore shutdown");
	// Since I introduced a Timer in ReloginAction the application will not shutdown properly
	System.exit(0);
    }

}
