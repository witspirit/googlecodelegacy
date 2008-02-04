/**
 * @author wItspirit
 * 23-okt-2005
 * ActionFactory.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import javax.swing.JProgressBar;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.AsyncFlickrFactory;
import be.vanvlerken.bert.flickrstore.gui.grouppoolselector.DownloadGroupPoolAction;
import be.vanvlerken.bert.flickrstore.gui.grouppoolselector.GroupPoolDescription;
import be.vanvlerken.bert.flickrstore.gui.grouppoolselector.VerifyGroupPoolAction;
import be.vanvlerken.bert.flickrstore.gui.photosetselector.DownloadPhotosetAction;
import be.vanvlerken.bert.flickrstore.gui.photosetselector.PhotosetDescription;
import be.vanvlerken.bert.flickrstore.gui.photosetselector.VerifyPhotosetAction;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

public class ActionFactory {
    private AsyncFlickrFactory flickrFactory;
    private CurrentUser currentUser;
    private ReloginAction reloginAction;
    private ExitAction exitAction;

    public ActionFactory(AsyncFlickrFactory flickrFactory, FlickrUser user) {
	this.flickrFactory = flickrFactory;
	currentUser = new CurrentUser(user);
	reloginAction = new ReloginAction(flickrFactory.getLoginManager(), currentUser);
	exitAction = new ExitAction();
    }

    public CurrentUser getCurrentUser() {
	return currentUser;
    }

    public ReloginAction getReloginAction() {
	return reloginAction;
    }
    
    public ExitAction getExitAction() {
	return exitAction;
    }


    public VerifyPhotosetAction getVerifyPhotosetAction(StatusBar statusBar, UrlProvider urlProvider, PhotosetDescription photosetDescription) {
	return new VerifyPhotosetAction(flickrFactory.getBrowser(), statusBar, urlProvider, photosetDescription);
    }

    public DownloadPhotosetAction getDownloadPhotosetAction(StatusBar statusBar, JProgressBar progressBar, UrlProvider urlProvider) {
	return new DownloadPhotosetAction(flickrFactory.getBrowser(), statusBar, progressBar, flickrFactory.getStore(), urlProvider);
    }

    public VerifyGroupPoolAction getVerifyGroupPoolAction(StatusBar statusBar, UrlProvider urlProvider, GroupPoolDescription groupPoolDescription) {
	return new VerifyGroupPoolAction(flickrFactory.getBrowser(), statusBar, urlProvider, groupPoolDescription);
    }

    public DownloadGroupPoolAction getDownloadGroupPoolAction(StatusBar statusBar, JProgressBar progressBar, UrlProvider urlProvider) {
	return new DownloadGroupPoolAction(flickrFactory.getBrowser(), statusBar, progressBar, flickrFactory.getStore(), urlProvider);
    }
    

}
