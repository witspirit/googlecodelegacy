package be.vanvlerken.bert.flickrstore.clipboardobserver;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClipboardObserver implements Runnable {

    private static final int DEFAULT_SLEEP_TIME = 1000;
    private final Clipboard clipboard;
    private final DataFlavor observedFlavor;
    private boolean observing;
    private Object lastData; // Stores what we have last read, to see if anything has changed
    
    private final List<ClipboardListener> listeners = new ArrayList<ClipboardListener>();

    public ClipboardObserver(Clipboard clipboard, DataFlavor observedFlavor) {
	this.clipboard = clipboard;
	this.observedFlavor = observedFlavor;
    }
    
    public synchronized void start() {
	observing = true;
	lastData = null;
	Thread observerThread = new Thread(this, "ClipboardObserver");
	observerThread.start();
    }
    
    public synchronized void stop() {
	observing = false;
    }

    public void run() {
	while (isObserving()) {
	    if (clipboard.isDataFlavorAvailable(observedFlavor)) {
		try {
		    Object data = clipboard.getData(observedFlavor);
		    if (isNewData(data)) {
			lastData = data;
			ClipboardEvent event = new ClipboardEvent(this, data);
			notifyListeners(event);			
		    }
		} catch (UnsupportedFlavorException e) {
		    // If this occurs, it actually means that the clipboard has been modified after our check...
		    // We do not care, we just ignore the contents in that case.
		} catch (IOException e) {
		    // This means we attempted to read valid data, but had a problem...
		    // Let's just report barebones, but proceed as normal afterwards
		    e.printStackTrace();
		}
	    }
	    
	    try {
		Thread.sleep(DEFAULT_SLEEP_TIME);
	    } catch (InterruptedException e) {
		// Just waking up...
	    }
	}	
    }

    private boolean isNewData(Object data) {
	return data != null && !data.equals(lastData);
    }
    
    public synchronized boolean isObserving() {
	return observing;
    }

    public void addClipboardListener(ClipboardListener listener) {
	synchronized (listeners) {
	    listeners.add(listener);
	}
    }
    
    public boolean removeClipboardListener(ClipboardListener listener) {
	synchronized (listeners) {
	    return listeners.remove(listener);
	}
    }
    
    private void notifyListeners(ClipboardEvent event) {
	List<ClipboardListener> safeListeners;
	synchronized (listeners) {
	    safeListeners = new ArrayList<ClipboardListener>(listeners);
	}
	for (ClipboardListener listener : safeListeners) {
	    listener.dataAvailable(event);
	}	
    }    
}
