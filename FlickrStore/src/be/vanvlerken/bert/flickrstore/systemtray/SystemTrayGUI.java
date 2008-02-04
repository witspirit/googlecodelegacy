package be.vanvlerken.bert.flickrstore.systemtray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import be.vanvlerken.bert.flickrstore.AsyncFlickrFactory;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardEvent;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardListener;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardObserver;
import be.vanvlerken.bert.flickrstore.gui.ActionFactory;
import be.vanvlerken.bert.flickrstore.gui.AuthorizationProcess;
import be.vanvlerken.bert.flickrstore.gui.CurrentUser;
import be.vanvlerken.bert.flickrstore.gui.ReloginAction;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

public class SystemTrayGUI {
    private final SystemTray systemTray;
    private final ActionFactory actionFactory;
    private final ClipboardObserver clipboardObserver;
    
    private TrayIcon trayIcon;
    
    
    
    public SystemTrayGUI() {
	if (!SystemTray.isSupported()) {
	    throw new UnsupportedOperationException("This system does not support system tray functionality.");
	}
	systemTray = SystemTray.getSystemTray();
		
	try {
	    actionFactory = initializeFlickr();
	} catch (Exception e) {
	    throw new IllegalStateException("Could not initialize Flickr connection");
	}
	
	clipboardObserver = new ClipboardObserver(Toolkit.getDefaultToolkit().getSystemClipboard(), DataFlavor.stringFlavor);
	clipboardObserver.addClipboardListener(new FlickrListener());
	
	PopupMenu menu = new PopupMenu();	
	menu.add(getReloginItem());
	menu.addSeparator();
	menu.add(getExitItem());

	Image image = getTrayImage();
	trayIcon = new TrayIcon(image, "FlickrStore", menu);
	trayIcon.setImageAutoSize(true);			
    }

    private MenuItem getReloginItem() {	
	MenuItem reloginItem = new MenuItem("Relogin");
	ReloginAction reloginAction = actionFactory.getReloginAction();
	reloginItem.addActionListener(reloginAction);
	return reloginItem;
    }

    private MenuItem getExitItem() {
	MenuItem exitItem = new MenuItem("Exit");	
	exitItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent actionEvent) {
		clipboardObserver.stop();
		System.exit(0);
	    }});
	return exitItem;
    }

    private Image getTrayImage() {
	URL imageUrl = this.getClass().getClassLoader().getResource("resources/FlickrIcon.png");
	Image image = Toolkit.getDefaultToolkit().getImage(imageUrl);
	return image;
    }
    
    private ActionFactory initializeFlickr() throws InterruptedException, ExecutionException {
	AuthorizationProcess authProcess = new AuthorizationProcess();
	AsyncFlickrFactory flickrFactory = new AsyncFlickrFactory(authProcess);

	Future<FlickrUser> flickrUserFuture = flickrFactory.getLoginManager().getUser();
	FlickrUser user = flickrUserFuture.get();
	
	return new ActionFactory(flickrFactory, user);
    }

    public void start() throws AWTException {
	clipboardObserver.start();
	systemTray.add(trayIcon);
	
	
	CurrentUser currentUser = actionFactory.getCurrentUser();
	currentUser.addObserver(new Observer() {
	    public void update(Observable observable, Object arg1) {
		String username = ((CurrentUser) observable).getFlickrUser().getUsername();
		trayIcon.setToolTip("FlickrStore ("+username+")");
		trayIcon.displayMessage(null, "Logged in as "+username, MessageType.INFO);
	    }});
	
	String username = currentUser.getFlickrUser().getUsername();
	trayIcon.setToolTip("FlickrStore ("+username+")");
	trayIcon.displayMessage(null, "Logged in as "+username, MessageType.INFO);		
    }
    
    private class FlickrListener implements ClipboardListener {

	public void dataAvailable(ClipboardEvent event) {
	    String clipboardData = event.getData();
	    
	    if (clipboardData.contains("flickr.com/")) {
		System.out.println("Potential flickr URL detected: "+clipboardData);
	    }
	    
	}
	
    }
}
