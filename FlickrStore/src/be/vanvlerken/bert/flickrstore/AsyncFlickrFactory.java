/**
 * @author wItspirit
 * 28-okt-2005
 * AsyncFlickrFactory.java
 */

package be.vanvlerken.bert.flickrstore;

import be.vanvlerken.bert.flickrstore.browser.AsyncBrowser;
import be.vanvlerken.bert.flickrstore.login.AsyncLoginManager;
import be.vanvlerken.bert.flickrstore.login.AuthenticatedExecutor;
import be.vanvlerken.bert.flickrstore.login.AuthorizationHandler;
import be.vanvlerken.bert.flickrstore.store.AsyncStore;

public class AsyncFlickrFactory extends FlickrFactory
{
    private AsyncLoginManager loginManager;
    private AsyncBrowser      browser;
    private AsyncStore        store;

    private AuthenticatedExecutor   executor;

    public AsyncFlickrFactory(AuthorizationHandler authHandler)
    {
        super();
        SyncFlickrFactory flickrFactory = new SyncFlickrFactory();
        executor = new AuthenticatedExecutor(flickrFactory.getLoginManager(), authHandler);

        loginManager = new AsyncLoginManager(executor, flickrFactory.getLoginManager());
        browser = new AsyncBrowser(executor, flickrFactory.getBrowser());
        store = new AsyncStore(executor, flickrFactory.getStore());
    }
    
    public AsyncLoginManager getLoginManager()
    {
        return loginManager;
    }

    public AsyncBrowser getBrowser()
    {
        return browser;
    }

    public AsyncStore getStore()
    {
        return store;
    }
}
