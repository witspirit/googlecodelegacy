package be.vanvlerken.bert.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardEvent;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardListener;
import be.vanvlerken.bert.flickrstore.clipboardobserver.ClipboardObserver;

public class ClipboardWatcher implements ClipboardListener {    
    private final ClipboardObserver observer;
    
    public ClipboardWatcher() {
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	observer = new ClipboardObserver(clipboard, DataFlavor.stringFlavor);
	observer.addClipboardListener(this);
    }
    
    public static void main(String... args) throws IOException {
	ClipboardWatcher clipboardWatcher = new ClipboardWatcher();
	clipboardWatcher.start();
	
	System.out.println("Watching the clipboard... Press ENTER to stop");
	System.in.read();
	clipboardWatcher.stop();
	System.out.println("Clipboard watching ended");
    }

    private void start() {
	observer.start();
    }
    
    private void stop() {
	observer.stop();	
    }

    public void dataAvailable(ClipboardEvent event) {
	System.out.println("Received String clipboard data: ");
	System.out.println(event.getData());
	System.out.println("---- End of Data ----");	
    }
}
