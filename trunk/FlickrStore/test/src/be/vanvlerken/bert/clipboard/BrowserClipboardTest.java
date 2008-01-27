package be.vanvlerken.bert.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import junit.framework.TestCase;

public class BrowserClipboardTest extends TestCase {

    public void testClipboardContents() throws ClassNotFoundException, UnsupportedFlavorException, IOException {
	Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	System.out.println("Current Clipboard contents are available in the following data flavors:");
	for (DataFlavor dataFlavor : systemClipboard.getAvailableDataFlavors() ) {
	    System.out.println("- "+dataFlavor.getHumanPresentableName()+" => "+dataFlavor.getMimeType());	    
	}
		
	assertTrue(systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor));
	String string = (String) systemClipboard.getData(DataFlavor.stringFlavor);
	System.out.println("Contents of clipboard as String: ");
	System.out.println(string);
    }
    
    public void testClipboardListener() throws ClassNotFoundException, IOException {
	Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();	
		
	systemClipboard.addFlavorListener(new FlavorListener() {

	    public void flavorsChanged(FlavorEvent fe) {
		System.out.println("Flavors Changed: "+fe);		
	    }});
	
	System.out.println("Starting to wait for flavors changed events...");
	System.in.read();	
    }
}
