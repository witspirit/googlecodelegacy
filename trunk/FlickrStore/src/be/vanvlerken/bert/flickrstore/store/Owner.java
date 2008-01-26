/**
 * @author wItspirit
 * 9-okt-2005
 * Owner.java
 */

package be.vanvlerken.bert.flickrstore.store;

import com.aetrion.flickr.people.User;

/**
 * Stores owner information
 */
public class Owner {
    private String username;
    private String realname;
    private String userId;

    public Owner(User user) {
	username = user.getUsername();
	realname = user.getRealName();
	userId = user.getId();
    }

    public String getRealname() {
	return realname;
    }

    public String getUserId() {
	return userId;
    }

    public String getUsername() {
	return username;
    }

}
