/**
 * @author wItspirit
 * 16-okt-2005
 * FlickrUser.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.util.Date;

import com.aetrion.flickr.contacts.OnlineStatus;


public interface FlickrUser
{
    public String getAwayMessage();
    public long getBandwidthMax();
    public long getBandwidthUsed();
    public long getFilesizeMax();
    public int getIconServer();
    public String getId();
    public String getLocation();
    public OnlineStatus getOnline();
    public int getPhotosCount();
    public Date getPhotosFirstDate();
    public Date getPhotosFirstDateTaken();
    public String getRealName();
    public String getUsername();
    public boolean isAdmin();
    public boolean isPro();    
}
