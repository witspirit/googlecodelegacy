/**
 * @author wItspirit
 * 8-okt-2005
 * FlickrTest.java
 */

package be.vanvlerken.bert.flickrstore.browser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.xml.sax.SAXException;

import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.groups.Group;
import com.aetrion.flickr.groups.GroupsInterface;
import com.aetrion.flickr.groups.pools.PoolsInterface;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photosets.Photoset;
import com.aetrion.flickr.photosets.Photosets;
import com.aetrion.flickr.photosets.PhotosetsInterface;
import com.aetrion.flickr.urls.UrlsInterface;

/**
 * This class is responsible for browsing Flickr photo collections
 */
public class SyncBrowser
{
    private Flickr flickr = null;

    public SyncBrowser(Flickr flickr)
    {
        this.flickr = flickr;
    }

    /**
     * @see be.vanvlerken.bert.flickrstore.browser.Browser#getSets(be.vanvlerken.bert.flickrstore.login.FlickrUser)
     */
    public List<Photoset> getSets(FlickrUser user) throws FlickrCommunicationException
    {
        try
        {
            List<Photoset> photosetList = new ArrayList<Photoset>();
            PhotosetsInterface sets = flickr.getPhotosetsInterface();
            Photosets setList = sets.getList(user.getId());

            Collection setListCollection = setList.getPhotosets();
            Iterator it = setListCollection.iterator();
            while (it.hasNext())
            {
                Photoset set = (Photoset) it.next();
                photosetList.add(set);
            }
            return photosetList;
        }
        catch (IOException e)
        {
            throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
        }
        catch (SAXException e)
        {
            throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
        }
        catch (FlickrException e)
        {
            throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
        }
    }

    /**
     * @see be.vanvlerken.bert.flickrstore.browser.Browser#getPhotosInSet(java.lang.String)
     */
    public List<Photo> getPhotosInSet(String setId) throws FlickrCommunicationException
    {
        try
        {
            PhotosetsInterface sets = flickr.getPhotosetsInterface();
            Collection photoCollection = sets.getPhotos(setId);

            return fetchPhotoInfo(photoCollection);
        }
        catch (IOException e)
        {
            throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
        }
        catch (SAXException e)
        {
            throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
        }
        catch (FlickrException e)
        {
            throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
        }
    }

    public Photoset getPhotoset(String setId) throws FlickrCommunicationException
    {
        PhotosetsInterface sets = flickr.getPhotosetsInterface();
        Photoset photoset = null;
        try
        {
            photoset = sets.getInfo(setId);
        }
        catch (IOException e)
        {
            throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
        }
        catch (SAXException e)
        {
            throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
        }
        catch (FlickrException e)
        {
            throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
        }
        return photoset;
    }

    public Icon getMiniPhoto(Photo photo) throws FlickrCommunicationException
    {
        Icon icon = null;
        try
        {
            PhotosInterface photosI = flickr.getPhotosInterface();
            Photo fullPhoto = photosI.getInfo(photo.getId(), null);
            icon = new ImageIcon(fullPhoto.getSmallSquareImage());            
        }
        catch (IOException e)
        {
            throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
        }
        catch (SAXException e)
        {
            throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
        }
        catch (FlickrException e)
        {
            throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
        }
        return icon;
    }

    public Group getGroupPoolInfo(String url) throws FlickrCommunicationException
    {
        try
        {
            GroupsInterface groupsI = flickr.getGroupsInterface();
            UrlsInterface urlsI = flickr.getUrlsInterface();
            Group group = urlsI.lookupGroup(url);
            group = groupsI.getInfo(group.getId());
            return group;
        }
        catch (IOException e)
        {
            throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
        }
        catch (SAXException e)
        {
            throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
        }
        catch (FlickrException e)
        {
            throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
        }
    }

    public List<Photo> getGroupPhotos(String groupId) throws FlickrCommunicationException
    {
        try
        {
            PoolsInterface poolsI = flickr.getPoolsInterface();
            Collection photoCollection = poolsI.getPhotos(groupId, null, Integer.MAX_VALUE, 0);
            return fetchPhotoInfo(photoCollection);
        }
        catch (IOException e)
        {
            throw new FlickrCommunicationException("Failed to connect to Flickr.", e);
        }
        catch (SAXException e)
        {
            throw new FlickrCommunicationException("Could not understand response from Flickr.", e);
        }
        catch (FlickrException e)
        {
            throw new FlickrCommunicationException("Flickr returned an error: " + e.getErrorMessage(), e);
        }
    }

    private List<Photo> fetchPhotoInfo(Collection photoCollection) throws IOException, SAXException, FlickrException
    {
        List<Photo> photoList = new ArrayList<Photo>();
        PhotosInterface photoI = flickr.getPhotosInterface();

        Iterator it = photoCollection.iterator();
        while (it.hasNext())
        {
            Photo photo = (Photo) it.next();
            photo = photoI.getInfo(photo.getId(), null);
            photoList.add(photo);
        }
        return photoList;
    }
}
