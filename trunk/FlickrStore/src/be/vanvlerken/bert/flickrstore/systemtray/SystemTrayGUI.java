package be.vanvlerken.bert.flickrstore.systemtray;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.DataFlavor;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import be.vanvlerken.bert.flickrstore.AsyncFlickrFactory;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardEvent;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardListener;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardObserver;
import be.vanvlerken.bert.flickrstore.gui.common.AuthorizationProcess;
import be.vanvlerken.bert.flickrstore.gui.common.CurrentUser;
import be.vanvlerken.bert.flickrstore.gui.common.ReloginAction;
import be.vanvlerken.bert.flickrstore.gui.common.ExitAction;
import be.vanvlerken.bert.flickrstore.gui.systray.SysTrayActionFactory;
import be.vanvlerken.bert.flickrstore.gui.systray.ConfigAction;
import be.vanvlerken.bert.flickrstore.gui.systray.JXTrayIcon;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

import javax.swing.*;

public class SystemTrayGUI {
    private final SystemTray systemTray;
    private final SysTrayActionFactory actionFactory;
    private final ClipboardObserver clipboardObserver;

    private JXTrayIcon trayIcon;


    public SystemTrayGUI() {
        if (!SystemTray.isSupported()) {
            throw new UnsupportedOperationException("This system does not support system tray functionality.");
        }       

        systemTray = SystemTray.getSystemTray();        

        clipboardObserver = new ClipboardObserver(Toolkit.getDefaultToolkit().getSystemClipboard(), DataFlavor.stringFlavor);
        clipboardObserver.addClipboardListener(new FlickrListener());

        try {
            actionFactory = initializeFlickr(new Runnable() {
                public void run() {
                    clipboardObserver.stop();
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Flickr connection");
        }

        JPopupMenu menu = new JPopupMenu();
        menu.add(getReloginItem());
        menu.add(getConfigItem());
        menu.addSeparator();
        menu.add(getExitItem());
        
        Image image = getTrayImage();
        trayIcon = new JXTrayIcon(image);
        trayIcon.setToolTip("FlickrStore");
        trayIcon.setJPopuMenu(menu);
        trayIcon.setImageAutoSize(true);
    }

    private JMenuItem getConfigItem() {
        ConfigAction configAction = actionFactory.getConfigAction();
        return new JMenuItem(configAction);
    }

    private JMenuItem getReloginItem() {
        ReloginAction reloginAction = actionFactory.getReloginAction();
        return new JMenuItem(reloginAction);
    }

    private JMenuItem getExitItem() {
        ExitAction exitAction = actionFactory.getExitAction();
        return new JMenuItem(exitAction);
    }

    private Image getTrayImage() {
        URL imageUrl = this.getClass().getClassLoader().getResource("resources/FlickrIcon.png");
        return Toolkit.getDefaultToolkit().getImage(imageUrl);
    }

    private SysTrayActionFactory initializeFlickr(Runnable exitCallback) throws InterruptedException, ExecutionException {
        AuthorizationProcess authProcess = new AuthorizationProcess();
        AsyncFlickrFactory flickrFactory = new AsyncFlickrFactory(authProcess);

        Future<FlickrUser> flickrUserFuture = flickrFactory.getLoginManager().getUser();
        FlickrUser user = flickrUserFuture.get();

        return new SysTrayActionFactory(flickrFactory, user, exitCallback);
    }

    public void start() throws AWTException {
        clipboardObserver.start();
        systemTray.add(trayIcon);


        CurrentUser currentUser = actionFactory.getCurrentUser();
        currentUser.addObserver(new Observer() {                                                     
            public void update(Observable observable, Object arg1) {
                String username = ((CurrentUser) observable).getFlickrUser().getUsername();
                trayIcon.setToolTip("FlickrStore (" + username + ")");
                trayIcon.displayMessage(null, "Logged in as " + username, MessageType.INFO);
            }
        });

        String username = currentUser.getFlickrUser().getUsername();
        trayIcon.setToolTip("FlickrStore (" + username + ")");
        trayIcon.displayMessage(null, "Logged in as " + username, MessageType.INFO);
    }

    private class FlickrListener implements ClipboardListener {

        public void dataAvailable(ClipboardEvent event) {
            String clipboardData = event.getData();

            if (clipboardData.contains("flickr.com/")) {
                System.out.println("Potential flickr URL detected: " + clipboardData);
            }

        }

    }
}
