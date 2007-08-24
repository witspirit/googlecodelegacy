/**
 * @author wItspirit
 * 30-okt-2005
 * DownloadAction.java
 */

package be.vanvlerken.bert.flickrstore.gui.grouppoolselector;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import be.vanvlerken.bert.components.gui.applicationwindow.StatusBar;
import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.browser.AsyncBrowser;
import be.vanvlerken.bert.flickrstore.gui.UrlProvider;
import be.vanvlerken.bert.flickrstore.store.AsyncStore;
import be.vanvlerken.bert.flickrstore.store.OverwriteNever;
import be.vanvlerken.bert.flickrstore.store.OverwriteStrategy;
import be.vanvlerken.bert.flickrstore.store.StoreProgress;

import com.aetrion.flickr.groups.Group;
import com.aetrion.flickr.photos.Photo;

public class DownloadGroupPoolAction extends AbstractAction
{
    private static final int ERROR_DISPLAY_TIME = 3000;
    private static final int POLLING_INTERVAL = 500;
    private Timer            timer;

    private AsyncBrowser     browser;
    private AsyncStore       store;
    private UrlProvider      urlProvider;
    private StatusBar statusBar;
    private int messageLevel;
    private JProgressBar progressBar;

    public DownloadGroupPoolAction(AsyncBrowser browser, StatusBar statusBar, JProgressBar progressBar, AsyncStore store, UrlProvider urlProvider)
    {
        super("Download");
        this.browser = browser;
        this.statusBar = statusBar;
        this.progressBar = progressBar;
        this.store = store;
        this.urlProvider = urlProvider;

        timer = new Timer("FlickrResponse-Polling", true);
    }

    private abstract class FlickrTimerTask<T> extends TimerTask
    {
        private Future<T> future;
        
        public FlickrTimerTask(Future<T> future)
        {
            this.future = future;
        }
        
        @Override
        public void run()
        {
            if (future.isDone())
            {
                // Don't reschedule and start storage
                try
                {
                    T result = future.get();
                    doWork(result);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    setEnabled(true);
                    statusBar.setMessage(messageLevel, e.getMessage(), ERROR_DISPLAY_TIME);
                    progressBar.setValue(0);
                }
                catch (ExecutionException e)
                {
                    Throwable cause = e.getCause();
                    if (cause instanceof FlickrCommunicationException)
                    {
                        setEnabled(true);
                        statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
                        progressBar.setValue(0);
                        JOptionPane.showMessageDialog(null, cause.getMessage()+"\n"+cause.getCause().getMessage(), "Communication problem",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        e.printStackTrace();
                        setEnabled(true);
                        statusBar.setMessage(messageLevel, cause.getMessage(), ERROR_DISPLAY_TIME);
                        progressBar.setValue(0);
                    }
                }
            }
            else
            {
                // Make sure we get called again
                timer.schedule(getScheduledTask(future), POLLING_INTERVAL);
                // System.out.println("Ivoke again...");
            }
        }
        
        protected void schedule(FlickrTimerTask<?> flickrTask)
        {
            timer.schedule(flickrTask, POLLING_INTERVAL);
        }
        
        protected abstract FlickrTimerTask<T> getScheduledTask(Future<T> future);
        
        protected abstract void doWork(T result);
                
    }
    
    private class VerifyGroupPool extends FlickrTimerTask<Group>
    {

        public VerifyGroupPool(Future<Group> future)
        {
            super(future);
        }

        @Override
        protected FlickrTimerTask<Group> getScheduledTask(Future<Group> groupFuture)
        {
            return new VerifyGroupPool(groupFuture);
        }

        @Override
        protected void doWork(Group group)
        {
            Future<List<Photo>> photosFuture = browser.getGroupPhotos(group.getId());
            String targetFolder = getTargetFolder();
            schedule(new WaitForPhotos(targetFolder, photosFuture));
        }
    }
    
    private class WaitForPhotos extends FlickrTimerTask<List<Photo>>
    {
        private String              targetFolder;

        public WaitForPhotos(String targetFolder,Future<List<Photo>> photosFuture)
        {
            super(photosFuture);
            this.targetFolder = targetFolder;
        }

        @Override
        protected FlickrTimerTask<List<Photo>> getScheduledTask(Future<List<Photo>> photosFuture)
        {
            return new WaitForPhotos(targetFolder, photosFuture);
        }

        @Override
        protected void doWork(List<Photo> photos)
        {
            OverwriteStrategy overwriteStrategy;
            // overwriteStrategy = new AskUserOverwriteStrategy();
            overwriteStrategy = new OverwriteNever();
            Future<Object> saveFuture = store.savePhotos(photos, targetFolder, new ProgressTracker(), overwriteStrategy);
            schedule(new WaitForSaveComplete(saveFuture));
        }
    }
    
    private class WaitForSaveComplete extends FlickrTimerTask<Object>
    {
        public WaitForSaveComplete(Future<Object> saveFuture)
        {
            super(saveFuture);
        }

        @Override
        protected FlickrTimerTask<Object> getScheduledTask(Future<Object> saveFuture)
        {
            return new WaitForSaveComplete(saveFuture);
        }

        @Override
        protected void doWork(Object result)
        {
            setEnabled(true);
            statusBar.setMessage(messageLevel, "Download complete", 2000);
            progressBar.setValue(0);
        }
    }    

    public void actionPerformed(ActionEvent ae)
    {
        setEnabled(false);
        Future<Group> groupFuture = browser.getGroup(urlProvider.getUrl());
        messageLevel = statusBar.addMessage("Downloading...");
        timer.schedule(new VerifyGroupPool(groupFuture), POLLING_INTERVAL);
    }

    private String getTargetFolder()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = fileChooser.showDialog(null, "Select Download Folder");
        if (response == JFileChooser.APPROVE_OPTION) { return fileChooser.getSelectedFile().toString(); }
        return null;
    }
    
    private class ProgressTracker implements StoreProgress
    {        
        public void setMaximum(int max)
        {
            progressBar.setMinimum(0);
            progressBar.setMaximum(max);
        }

        public void setCurrent(int current)
        {
            progressBar.setValue(current);
        }        
    }
}
