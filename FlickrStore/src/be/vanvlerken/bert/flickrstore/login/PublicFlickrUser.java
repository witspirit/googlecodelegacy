/**
 * @author wItspirit
 * 5-nov-2005
 * PublicFlickrUser.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.util.Date;

import com.aetrion.flickr.contacts.OnlineStatus;


public class PublicFlickrUser implements FlickrUser
{

    public String getAwayMessage()
    {
        return null;
    }

    public int getBandwidthMax()
    {
        return 0;
    }

    public int getBandwidthUsed()
    {
        return 0;
    }

    public int getFilesizeMax()
    {
        return 0;
    }

    public int getIconServer()
    {
        return 0;
    }

    public String getId()
    {
        return null;
    }

    public String getLocation()
    {
        return null;
    }

    public OnlineStatus getOnline()
    {
        return null;
    }

    public int getPhotosCount()
    {
        return 0;
    }

    public Date getPhotosFirstDate()
    {
        return null;
    }

    public Date getPhotosFirstDateTaken()
    {
        return null;
    }

    public String getRealName()
    {
        return null;
    }

    public String getUsername()
    {
        return "public";
    }

    public boolean isAdmin()
    {
        return false;
    }

    public boolean isPro()
    {
        return false;
    }

}
