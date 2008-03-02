/**
 * @author wItspirit
 * 30-okt-2005
 * DownloadAction.java
 */

package be.vanvlerken.bert.flickrstore.gui.photosetselector;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.browser.AsyncBrowser;
import be.vanvlerken.bert.flickrstore.gui.AskUserOverwriteStrategy;
import be.vanvlerken.bert.flickrstore.gui.UrlProvider;
import be.vanvlerken.bert.flickrstore.gui.UrlUtils;
import be.vanvlerken.bert.flickrstore.store.AsyncStore;
import be.vanvlerken.bert.flickrstore.store.StoreProgress;
import com.aetrion.flickr.photos.Photo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DownloadPhotosetAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private static final int ERROR_DISPLAY_TIME = 3000;
    private static final int POLLING_INTERVAL = 500;
    private Timer timer;

    private AsyncBrowser browser;
    private AsyncStore store;
    private UrlProvider urlProvider;
    private StatusBar statusBar;
    private int messageLevel;
    private JProgressBar progressBar;

    public DownloadPhotosetAction(AsyncBrowser browser, StatusBar statusBar, JProgressBar progressBar, AsyncStore store, UrlProvider urlProvider) {
        super("Download");
        this.browser = browser;
        this.statusBar = statusBar;
        this.progressBar = progressBar;
        this.store = store;
        this.urlProvider = urlProvider;

        timer = new Timer("FlickrResponse-Polling", true);
    }

    private class WaitForPhotos extends TimerTask {
        private Future<List<Photo>> photosFuture;
        private String targetFolder;

        public WaitForPhotos(Future<List<Photo>> photosFuture, String targetFolder) {
            this.photosFuture = photosFuture;
            this.targetFolder = targetFolder;
        }

        @Override
        public void run() {
            if (photosFuture.isDone()) {
                // Don't reschedule and start storage
                try {
                    List<Photo> photos = photosFuture.get();
                    Future<?> saveFuture = store.savePhotos(photos, targetFolder, new ProgressTracker(), new AskUserOverwriteStrategy());
                    timer.schedule(new WaitForSaveComplete(saveFuture), POLLING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setEnabled(true);
                    statusBar.setMessage(messageLevel, e.getMessage(), ERROR_DISPLAY_TIME);
                    progressBar.setValue(0);
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof FlickrCommunicationException) {
                        setEnabled(true);
                        statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
                        progressBar.setValue(0);
                        JOptionPane.showMessageDialog(null, cause.getMessage(), "Communication problem", JOptionPane.ERROR_MESSAGE);
                    } else {
                        e.printStackTrace();
                        setEnabled(true);
                        statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
                        progressBar.setValue(0);
                    }
                }
            } else {
                // Make sure we get called again
                timer.schedule(new WaitForPhotos(photosFuture, targetFolder), POLLING_INTERVAL);
                // System.out.println("Ivoke again...");
            }
        }
    }

    private class WaitForSaveComplete extends TimerTask {
        private Future<?> saveFuture;

        public WaitForSaveComplete(Future<?> saveFuture) {
            this.saveFuture = saveFuture;
        }

        @Override
        public void run() {
            if (saveFuture.isDone()) {
                setEnabled(true);
                statusBar.setMessage(messageLevel, "Download complete", 2000);
                progressBar.setValue(0);
            } else {
                // Make sure we get called again
                timer.schedule(new WaitForSaveComplete(saveFuture), POLLING_INTERVAL);
                // System.out.println("Ivoke again...");
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        setEnabled(false);
        String setId = UrlUtils.extractPhotosetId(urlProvider.getUrl());
        if (setId != null) {
            // Ok, there is something to download
            Future<List<Photo>> photosFuture = browser.getPhotosInSet(setId);
            // Request a target folder - via a FileChooser
            String targetFolder = getTargetFolder();
            System.out.println("TargetFolder = " + targetFolder);
            if (targetFolder != null) {
                messageLevel = statusBar.addMessage("Downloading...");
                timer.schedule(new WaitForPhotos(photosFuture, targetFolder), POLLING_INTERVAL);
            } else {
                setEnabled(true);
                statusBar.addMessage("Download aborted. No valid target folder selected.", ERROR_DISPLAY_TIME);
            }
        } else {
            setEnabled(true);
            statusBar.addMessage("Invalid Photoset URL", ERROR_DISPLAY_TIME);
        }
    }

    private String getTargetFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = fileChooser.showDialog(null, "Select Download Folder");
        if (response == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toString();
        }
        return null;
    }

    private class ProgressTracker implements StoreProgress {
        public void setMaximum(int max) {
            progressBar.setMinimum(0);
            progressBar.setMaximum(max);
        }

        public void setCurrent(int current) {
            progressBar.setValue(current);
        }
    }
}
