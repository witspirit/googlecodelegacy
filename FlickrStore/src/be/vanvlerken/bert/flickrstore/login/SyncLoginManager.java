/**
 * @author wItspirit
 * 8-okt-2005
 * FlickrTest.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;

import org.xml.sax.SAXException;

import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.people.User;

/**
 * This class is responsible for managing Login credentials to Flickr
 */
public class SyncLoginManager {
    private static final String tokenKey = "FLICKR_LOGIN_TOKEN";
    private static final String publicUserKey = "FLICKR_USE_PUBLIC_USER";

    private Flickr flickr = null;
    private Auth token = null;
    private String stringToken = null;
    private boolean publicUser = false;
    private Preferences prefs = null;

    private String sharedSecret;

    public SyncLoginManager(Flickr flickr, String sharedSecret) {
	this.flickr = flickr;
	this.sharedSecret = sharedSecret;

	prefs = Preferences.userNodeForPackage(SyncLoginManager.class);
	publicUser = prefs.getBoolean(publicUserKey, false);
	stringToken = prefs.get(tokenKey, null);
    }

    /**
     * @see be.vanvlerken.bert.flickrstore.login.LoginManager#login(be.vanvlerken.bert.flickrstore.login.AuthorizationHandler)
     */
    public void login(AuthorizationHandler authHandler) throws FlickrAuthorizationRequiredException, FlickrCommunicationException {
	assert (authHandler != null);

	RequestContext requestCtx = RequestContext.getRequestContext();
	requestCtx.setSharedSecret(sharedSecret);

	if (publicUser) {
	    return;
	}

	try {
	    AuthInterface auth = flickr.getAuthInterface();

	    if (stringToken != null) {
		try {
		    token = auth.checkToken(stringToken);
		} catch (FlickrException fe) {
		    // Most likely reason -> Token invalid.
		    // Resetting token
		    fe.printStackTrace(); // Currently for debug purposes
		    logout();
		}
	    }

	    if (stringToken == null) {
		String frob = auth.getFrob();
		URL url = auth.buildAuthenticationUrl(Permission.READ, frob);
		if (!authHandler.requestAuthorization(url)) {
		    // Store that the user prefers public operation
		    publicUser = true;
		    prefs.putBoolean(publicUserKey, publicUser);
		    return;
		}

		try {
		    token = auth.getToken(frob);
		    storeToken();
		} catch (FlickrException fe) {
		    // Most likely reason -> Not authorized
		    // fe.printStackTrace(); // Currently for debug purposes;
		    throw new FlickrAuthorizationRequiredException(
			    "This application has not been granted the appropriate rights. Please authorize the application.", fe);
		}
	    }

	    // One way or another, we have retrieved a token.
	    requestCtx.setAuth(token);
	} catch (IOException e) {
	    throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
	} catch (SAXException e) {
	    throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
	} catch (FlickrException e) {
	    e.printStackTrace();
	    throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
	}
    }

    public FlickrUser getUser() throws FlickrCommunicationException {
	if (publicUser) {
	    return new PublicFlickrUser();
	} else {
	    // Acquire user info
	    User user = token.getUser();
	    PeopleInterface people = flickr.getPeopleInterface();
	    // Populate all data
	    try {
		user = people.getInfo(user.getId());
		return new FlickrUserImpl(user);
	    } catch (IOException e) {
		throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
	    } catch (SAXException e) {
		throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
	    } catch (FlickrException e) {
		e.printStackTrace();
		throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
	    }
	}
    }

    public void logout() {
	token = null;
	stringToken = null;
	prefs.remove(tokenKey);
	RequestContext.getRequestContext().setAuth(null);

	publicUser = false;
	prefs.remove(publicUserKey);
    }

    /**
     * 
     */
    private void storeToken() {
	assert (token != null);

	stringToken = token.getToken();
	prefs.put(tokenKey, stringToken);
    }
}
