/**
 * @author wItspirit
 * 9-okt-2005
 * Visibility.java
 */

package be.vanvlerken.bert.flickrstore.store;

/**
 * Stores the visibility of a photo
 */
public class Visibility {
    private boolean family;
    private boolean friend;
    private boolean anyone;

    public Visibility(boolean family, boolean friend, boolean anyone) {
	this.family = family;
	this.friend = friend;
	this.anyone = anyone;
    }

    public boolean isAnyone() {
	return anyone;
    }

    public boolean isFamily() {
	return family;
    }

    public boolean isFriend() {
	return friend;
    }

}
