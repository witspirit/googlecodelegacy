/**
 * @author wItspirit
 * 24-okt-2005
 * ReloginAction.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.login.AsyncLoginManager;
import be.vanvlerken.bert.flickrstore.login.FlickrAuthorizationRequiredException;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

public class ReloginAction extends AbstractAction {    
    private static final long serialVersionUID = 1L;

    private static final int POLLING_INTERVAL = 500;

    private AsyncLoginManager loginMgr;
    private CurrentUser currentUser;

    private Timer timer;

    private class ChangeLoggedInUser extends TimerTask {
	private Future<Object> logoutFuture;

	public ChangeLoggedInUser(Future<Object> loginFuture) {
	    this.logoutFuture = loginFuture;
	}

	@Override
	public void run() {
	    if (logoutFuture.isDone()) {
		try {
		    // Don't reschedule and notify GUI
		    logoutFuture.get(); // Just to check exceptions !
		    Future<FlickrUser> flickrUserFuture = loginMgr.getUser();
		    timer.schedule(new FetchUserInfo(flickrUserFuture), POLLING_INTERVAL);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    Throwable cause = e.getCause();
		    if (cause instanceof FlickrAuthorizationRequiredException) {
			JOptionPane.showMessageDialog(null, cause.getMessage(), "Authorization Required", JOptionPane.ERROR_MESSAGE);
		    } else if (cause instanceof FlickrCommunicationException) {
			JOptionPane.showMessageDialog(null, cause.getMessage(), "Communication problem", JOptionPane.ERROR_MESSAGE);
		    } else {
			e.printStackTrace();
		    }
		}
	    } else {
		// Make sure we get called again
		timer.schedule(new ChangeLoggedInUser(logoutFuture), POLLING_INTERVAL);
		// System.out.println("Ivoke again...");
	    }
	}
    }

    private class FetchUserInfo extends TimerTask {
	private Future<FlickrUser> flickrUserFuture;

	public FetchUserInfo(Future<FlickrUser> flickrUserFuture) {
	    this.flickrUserFuture = flickrUserFuture;
	}

	@Override
	public void run() {
	    if (flickrUserFuture.isDone()) {
		// Don't reschedule and notify GUI
		try {
		    FlickrUser user = flickrUserFuture.get();
		    // Notify the GUI of the new user
		    // System.out.println("Relogin as "+user.getUsername());
		    currentUser.changeUser(user);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    Throwable cause = e.getCause();
		    if (cause instanceof FlickrCommunicationException) {
			JOptionPane.showMessageDialog(null, cause.getMessage(), "Communication problem", JOptionPane.ERROR_MESSAGE);
		    } else {
			e.printStackTrace();
		    }
		}
	    } else {
		// Make sure we get called again
		timer.schedule(new FetchUserInfo(flickrUserFuture), POLLING_INTERVAL);
		// System.out.println("Ivoke again...");
	    }
	}
    }

    public ReloginAction(AsyncLoginManager loginManager, CurrentUser currentUser) {
	super("Login as another user");
	this.loginMgr = loginManager;
	this.currentUser = currentUser;
	timer = new Timer("FlickrResponse-Polling", true);
    }

    public void actionPerformed(ActionEvent ae) {
	Future<Object> logoutFuture = loginMgr.logout();
	timer.schedule(new ChangeLoggedInUser(logoutFuture), POLLING_INTERVAL);
    }

}
