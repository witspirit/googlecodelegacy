package witspirit.appengine.winkelplanner;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

import witspirit.appengine.winkelplanner.web.lists.ListsPage;
import witspirit.appengine.winkelplanner.web.overview.OverviewPage;
import witspirit.appengine.winkelplanner.web.products.ProductsPage;
import witspirit.appengine.winkelplanner.web.shops.ShopsPage;

/**
 * Check out http://stronglytypedblog.blogspot.com/2009/04/wicket-on-google-app-engine.html
 * for some of the "special" settings that needed to be applied for AppEngine
 */
public class WinkelPlanner extends WebApplication {

    public WinkelPlanner() {
        this.mountBookmarkablePage("/lists", ListsPage.class);
        this.mountBookmarkablePage("/products", ProductsPage.class);
        this.mountBookmarkablePage("/shops", ShopsPage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return OverviewPage.class;
    }

    @Override
    protected void init() {
        // AppEngine does not allow the threads this polling creates
        getResourceSettings().setResourcePollFrequency(null);

    }

    @Override
    protected ISessionStore newSessionStore() {
        // AppEngine does not allow writing to disk, thus no DiskPageStore should be used
        // return new SecondLevelCacheSessionStore(this, new DiskPageStore());
        return new HttpSessionStore(this);
    }

}
