package witspirit.appengine.winkelplanner.web.common;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;

import witspirit.appengine.winkelplanner.web.lists.ListsPage;
import witspirit.appengine.winkelplanner.web.overview.OverviewPage;
import witspirit.appengine.winkelplanner.web.products.ProductsPage;
import witspirit.appengine.winkelplanner.web.shops.ShopsPage;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class BasePage extends WebPage {

    public BasePage() {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String targetUrl = getWebRequestCycle().getWebRequest().getURL();
        ExternalLink login = new ExternalLink("login", userService.createLoginURL(targetUrl));
        add(login);

        WebMarkupContainer userInfo = new WebMarkupContainer("userInfo");
        add(userInfo);
        Label username = new Label("username", user != null ? user.getNickname() : "-");
        userInfo.add(username);
        ExternalLink logout = new ExternalLink("logout", userService.createLogoutURL(targetUrl));
        userInfo.add(logout);

        if (user == null) {
            userInfo.setVisible(false);
        } else {
            login.setVisible(false);
        }
        
        registerNavigation("overviewLink", OverviewPage.class);
        registerNavigation("listsLink", ListsPage.class);
        registerNavigation("productsLink", ProductsPage.class);
        registerNavigation("shopsLink", ShopsPage.class);
    }
    
    private void registerNavigation(String id, Class<? extends Page> targetPage) {
        BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(id, targetPage);
        if (this.getPageClass() == targetPage) {
            link.add(new SimpleAttributeModifier("class", "currentPage"));
        }
        add(link);
    }

    protected User getUser() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.getCurrentUser();
    }

}
