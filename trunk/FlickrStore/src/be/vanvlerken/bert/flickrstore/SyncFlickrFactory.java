/**
 * @author wItspirit
 * 28-okt-2005
 * SyncFlickrFactory.java
 */

package be.vanvlerken.bert.flickrstore;

import be.vanvlerken.bert.flickrstore.browser.SyncBrowser;
import be.vanvlerken.bert.flickrstore.login.SyncLoginManager;
import be.vanvlerken.bert.flickrstore.store.SyncStore;

import com.aetrion.flickr.Flickr;

public class SyncFlickrFactory extends FlickrFactory {
    private Flickr flickr = null;

    private SyncLoginManager loginManager = null;
    private SyncBrowser browser = null;
    private SyncStore store = null;

    public SyncFlickrFactory() {
	super();
	flickr = new Flickr(apiKey);

	loginManager = new SyncLoginManager(flickr, sharedSecret);
	browser = new SyncBrowser(flickr);
	store = new SyncStore();
    }

    public SyncLoginManager getLoginManager() {
	return loginManager;
    }

    public SyncBrowser getBrowser() {
	return browser;
    }

    public SyncStore getStore() {
	return store;
    }

}
