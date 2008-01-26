/**
 * @author wItspirit
 * 29-okt-2005
 * VerifyAction.java
 */

package be.vanvlerken.bert.flickrstore.gui.photosetselector;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.browser.AsyncBrowser;
import be.vanvlerken.bert.flickrstore.gui.UrlProvider;
import be.vanvlerken.bert.flickrstore.gui.UrlUtils;

import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photosets.Photoset;

public class VerifyPhotosetAction extends AbstractAction {    
    private static final long serialVersionUID = 1L;
    
    private static final int POLLING_INTERVAL = 500;
    private Timer timer;

    private AsyncBrowser browser;
    private UrlProvider urlProvider;
    private PhotosetDescription photosetDescription;
    private StatusBar statusBar;
    private int messageLevel;
    public static final long ERROR_DISPLAY_TIME = 3000;

    private class VerifyPhotoset extends TimerTask {
	private Future<Photoset> photosetFuture;

	public VerifyPhotoset(Future<Photoset> photosetFuture) {
	    this.photosetFuture = photosetFuture;
	}

	@Override
	public void run() {
	    if (photosetFuture.isDone()) {
		// Don't reschedule and notify GUI
		try {
		    Photoset photoset = photosetFuture.get();
		    // Notify the GUI of the verified Photoset

		    String title = photoset.getTitle();
		    String description = photoset.getDescription();
		    int photoCount = photoset.getPhotoCount();

		    Photo photo = photoset.getPrimaryPhoto();
		    Future<Icon> iconFuture = browser.getMiniPhoto(photo);

		    timer.schedule(new UpdateGUI(iconFuture, title, description, photoCount), POLLING_INTERVAL);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		    setEnabled(true);
		    statusBar.setMessage(messageLevel, e.getMessage(), ERROR_DISPLAY_TIME);
		} catch (ExecutionException e) {
		    Throwable cause = e.getCause();
		    if (cause instanceof FlickrCommunicationException) {
			setEnabled(true);
			statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
			JOptionPane.showMessageDialog(null, cause.getMessage(), "Communication problem", JOptionPane.ERROR_MESSAGE);
		    } else {
			e.printStackTrace();
			setEnabled(true);
			statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
		    }
		}
	    } else {
		// Make sure we get called again
		timer.schedule(new VerifyPhotoset(photosetFuture), POLLING_INTERVAL);
		// System.out.println("Ivoke again...");
	    }
	}
    }

    private class UpdateGUI extends TimerTask {
	private String title;
	private String description;
	private int photoCount;
	private Future<Icon> iconFuture;

	public UpdateGUI(Future<Icon> iconFuture, String title, String description, int photoCount) {
	    this.iconFuture = iconFuture;
	    this.title = title;
	    this.description = description;
	    this.photoCount = photoCount;
	}

	@Override
	public void run() {
	    if (iconFuture.isDone()) {
		// Don't reschedule and notify GUI
		try {
		    // Notify the GUI of the verified Photoset
		    Icon icon = iconFuture.get();
		    photosetDescription.updateDescription(icon, title, description, photoCount);
		    statusBar.clearMessage(messageLevel);
		    statusBar.addMessage("Verify complete", 1500);
		    setEnabled(true);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		    setEnabled(true);
		    statusBar.setMessage(messageLevel, e.getMessage(), ERROR_DISPLAY_TIME);
		} catch (ExecutionException e) {
		    Throwable cause = e.getCause();
		    if (cause instanceof FlickrCommunicationException) {
			setEnabled(true);
			statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
			JOptionPane.showMessageDialog(null, cause.getMessage(), "Communication problem", JOptionPane.ERROR_MESSAGE);
		    } else {
			setEnabled(true);
			e.printStackTrace();
			statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
		    }
		}
	    } else {
		// Make sure we get called again
		timer.schedule(new UpdateGUI(iconFuture, title, description, photoCount), POLLING_INTERVAL);
		// System.out.println("Ivoke again...");
	    }
	}

    }

    public VerifyPhotosetAction(AsyncBrowser browser, StatusBar statusBar, UrlProvider urlProvider, PhotosetDescription photosetDescription) {
	super("Verify");
	this.browser = browser;
	this.statusBar = statusBar;
	this.urlProvider = urlProvider;
	this.photosetDescription = photosetDescription;

	timer = new Timer("FlickrResponse-Polling", true);
    }

    public void actionPerformed(ActionEvent ae) {
	setEnabled(false);
	String setId = UrlUtils.extractPhotosetId(urlProvider.getUrl());
	if (setId != null) {
	    Future<Photoset> photosetFuture = browser.getPhotoset(setId);
	    messageLevel = statusBar.addMessage("Verifying...");
	    timer.schedule(new VerifyPhotoset(photosetFuture), POLLING_INTERVAL);
	} else {
	    setEnabled(true);
	    statusBar.addMessage("Invalid photoset URL", 3000);
	}
    }

}
