/**
 * @author wItspirit
 * 16-okt-2005
 * FlickrAuthorizationRequiredException.java
 */

package be.vanvlerken.bert.flickrstore.login;

/**
 * Exception that indicates that the application does not have authorization to access private photos.
 */
public class FlickrAuthorizationRequiredException extends Exception {    
    private static final long serialVersionUID = 1L;

    public FlickrAuthorizationRequiredException() {
	super();
    }

    public FlickrAuthorizationRequiredException(String msg, Throwable t) {
	super(msg, t);
    }

    public FlickrAuthorizationRequiredException(String msg) {
	super(msg);
    }

    public FlickrAuthorizationRequiredException(Throwable t) {
	super(t);
    }
}
