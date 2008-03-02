/**
 * @author wItspirit
 * 29-okt-2005
 * VerifyAction.java
 */

package be.vanvlerken.bert.flickrstore.gui.grouppoolselector;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.browser.AsyncBrowser;
import be.vanvlerken.bert.flickrstore.gui.UrlProvider;
import com.aetrion.flickr.groups.Group;
import com.aetrion.flickr.photos.Photo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VerifyGroupPoolAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private static final int POLLING_INTERVAL = 500;
    private Timer timer;

    private AsyncBrowser browser;
    private UrlProvider urlProvider;
    private GroupPoolDescription groupPoolDescription;
    private StatusBar statusBar;
    private int messageLevel;
    public static final long ERROR_DISPLAY_TIME = 3000;

    private class VerifyGroupPool extends TimerTask {
        private Future<Group> groupFuture;

        public VerifyGroupPool(Future<Group> groupFuture) {
            this.groupFuture = groupFuture;
        }

        @Override
        public void run() {
            if (groupFuture.isDone()) {
                // Don't reschedule and notify GUI
                try {
                    Group group = groupFuture.get();
                    // Notify the GUI of the verified GroupPool

                    if (group != null) {
                        Future<List<Photo>> photosFuture = browser.getGroupPhotos(group.getId());
                        timer.schedule(new CompleteGroupDescription(group, photosFuture), POLLING_INTERVAL);
                    } else {
                        statusBar.setMessage(messageLevel, "Could not find GroupPool", ERROR_DISPLAY_TIME);
                        setEnabled(true);
                    }
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
                timer.schedule(new VerifyGroupPool(groupFuture), POLLING_INTERVAL);
                // System.out.println("Ivoke again...");
            }
        }
    }

    private class CompleteGroupDescription extends TimerTask {
        private Future<List<Photo>> photosFuture;
        private Group group;

        public CompleteGroupDescription(Group group, Future<List<Photo>> photosFuture) {
            this.group = group;
            this.photosFuture = photosFuture;
        }

        @Override
        public void run() {
            if (photosFuture.isDone()) {
                try {
                    List<Photo> photos = photosFuture.get();
                    // Notify the GUI of the verified GroupPool

                    if (photos.isEmpty()) {
                        String name = group.getName();
                        String description = group.getDescription();
                        groupPoolDescription.updateDescription(null, name, description, 0);

                        statusBar.setMessage(messageLevel, "Verify complete", 1500);
                        setEnabled(true);
                    } else {
                        // Go and fetch a mini photo !
                        Future<Icon> iconFuture = browser.getMiniPhoto(photos.get(0));
                        timer.schedule(new UpdateGUI(group, photos, iconFuture), POLLING_INTERVAL);
                    }
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
                timer.schedule(new CompleteGroupDescription(group, photosFuture), POLLING_INTERVAL);
                // System.out.println("Ivoke again...");
            }
        }
    }

    private class UpdateGUI extends TimerTask {
        private Future<Icon> iconFuture;
        private List<Photo> photos;
        private Group group;

        public UpdateGUI(Group group, List<Photo> photos, Future<Icon> iconFuture) {
            this.group = group;
            this.photos = photos;
            this.iconFuture = iconFuture;
        }

        @Override
        public void run() {
            if (iconFuture.isDone()) {
                try {
                    Icon icon = iconFuture.get();
                    // Notify the GUI of the verified GroupPool

                    String name = group.getName();
                    String description = group.getDescription();
                    int photoCount = photos.size();
                    groupPoolDescription.updateDescription(icon, name, description, photoCount);

                    statusBar.setMessage(messageLevel, "Verify complete", 1500);
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
                        e.printStackTrace();
                        setEnabled(true);
                        statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
                    }
                }
            } else {
                // Make sure we get called again
                timer.schedule(new UpdateGUI(group, photos, iconFuture), POLLING_INTERVAL);
                // System.out.println("Ivoke again...");
            }
        }
    }

    public VerifyGroupPoolAction(AsyncBrowser browser, StatusBar statusBar, UrlProvider urlProvider, GroupPoolDescription groupPoolDescription) {
        super("Verify");
        this.browser = browser;
        this.statusBar = statusBar;
        this.urlProvider = urlProvider;
        this.groupPoolDescription = groupPoolDescription;

        timer = new Timer("FlickrResponse-Polling", true);
    }

    public void actionPerformed(ActionEvent ae) {
        setEnabled(false);
        Future<Group> groupFuture = browser.getGroup(urlProvider.getUrl());
        messageLevel = statusBar.addMessage("Verifying...");
        timer.schedule(new VerifyGroupPool(groupFuture), POLLING_INTERVAL);
    }

}
