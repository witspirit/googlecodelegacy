/**
 * @author wItspirit
 * 16-okt-2005
 * FlickrUserImpl.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.util.Date;

import com.aetrion.flickr.contacts.OnlineStatus;
import com.aetrion.flickr.people.User;


public class FlickrUserImpl implements FlickrUser
{
    private User user;
    
    public FlickrUserImpl(User user)
    {
        super();
        this.user = user;
    }
    
    // Delegators
    
    public String getAwayMessage()
    {
        return user.getAwayMessage();
    }
    public int getBandwidthMax()
    {
        return user.getBandwidthMax();
    }
    public int getBandwidthUsed()
    {
        return user.getBandwidthUsed();
    }
    public int getFilesizeMax()
    {
        return user.getFilesizeMax();
    }
    public int getIconServer()
    {
        return user.getIconServer();
    }
    public String getId()
    {
        return user.getId();
    }
    public String getLocation()
    {
        return user.getLocation();
    }
    public OnlineStatus getOnline()
    {
        return user.getOnline();
    }
    public int getPhotosCount()
    {
        return user.getPhotosCount();
    }
    public Date getPhotosFirstDate()
    {
        return user.getPhotosFirstDate();
    }
    public Date getPhotosFirstDateTaken()
    {
        return user.getPhotosFirstDateTaken();
    }
    public String getRealName()
    {
        return user.getRealName();
    }
    public String getUsername()
    {
        return user.getUsername();
    }
    public boolean isAdmin()
    {
        return user.isAdmin();
    }
    public boolean isPro()
    {
        return user.isPro();
    }

}
