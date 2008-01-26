/**
 * @author wItspirit
 * 19-okt-2005
 * LoginDialog.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import com.Ostermiller.util.Browser;

import be.vanvlerken.bert.flickrstore.login.AuthorizationHandler;

public class AuthorizationProcess implements AuthorizationHandler {
    public boolean requestAuthorization(URL url) {
	RequestAuthorizationDialog reqAuth = new RequestAuthorizationDialog();
	if (reqAuth.showDialog() == RequestAuthorizationDialog.DialogState.AUTHORIZE) {
	    System.out.println("Initiate authorization...");
	    try {
		Browser.init();
		Browser.displayURL(url.toString());
	    } catch (IOException e) {
		String errorMessage = "FlickrStore was unable to open a browser. Please visit " + url.toString()
			+ " in order to complete the authorization process.";
		JOptionPane.showMessageDialog(null, errorMessage, "Unable to open browser", JOptionPane.ERROR_MESSAGE);
	    }
	    CompleteAuthorizationDialog completeAuth = new CompleteAuthorizationDialog();
	    if (completeAuth.showDialog() == CompleteAuthorizationDialog.DialogState.COMPLETE_AUTHORIZE) {
		// We should be done
		System.out.println("Authorization complete");
		return true;
	    }
	}
	System.out.println("Authorization aborted.");
	return false;
    }

}
