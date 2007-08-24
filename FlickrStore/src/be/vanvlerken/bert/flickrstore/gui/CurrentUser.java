/**
 * @author wItspirit
 * 24-okt-2005
 * CurrentUser.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.util.Observable;

import be.vanvlerken.bert.flickrstore.login.FlickrUser;


public class CurrentUser extends Observable
{
    private FlickrUser flickrUser;
    
    public CurrentUser(FlickrUser flickrUser)
    {
        this.flickrUser = flickrUser;
    }
    
    public FlickrUser getFlickrUser()
    {
        return flickrUser;
    }
    
    public void changeUser(FlickrUser flickrUser)
    {
        if ( this.flickrUser != flickrUser && !this.flickrUser.equals(flickrUser) )
        {
            this.flickrUser = flickrUser;
            setChanged();
            notifyObservers();            
        }
    }

}
