/**
 * @author wItspirit
 * 8-okt-2005
 * FlickrTest.java
 */

package be.vanvlerken.bert.flickrstore.textui;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import be.vanvlerken.bert.flickrstore.FlickrCommunicationException;
import be.vanvlerken.bert.flickrstore.SyncFlickrFactory;
import be.vanvlerken.bert.flickrstore.browser.SyncBrowser;
import be.vanvlerken.bert.flickrstore.login.AuthorizationHandler;
import be.vanvlerken.bert.flickrstore.login.FlickrAuthorizationRequiredException;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;
import be.vanvlerken.bert.flickrstore.store.SyncStore;

import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photosets.Photoset;

/**
 * Simple class that handles the TextUI of the FlickrStore application
 */
public class TextUI implements AuthorizationHandler, Runnable
{
    private SyncFlickrFactory flickrFactory;
    
    public TextUI()
    {
        flickrFactory = new SyncFlickrFactory();
    }

    public void printUserStats(FlickrUser user)
    {
        System.out.println("Username        : " + user.getUsername());
        System.out.println("Real name       : " + user.getRealName());
        System.out.println("Pro user        : " + user.isPro());
        System.out.println("Admin user      : " + user.isAdmin());
        System.out.println("Away Message    : " + user.getAwayMessage());
        System.out.println("Max Bandwith    : " + user.getBandwidthMax());
        System.out.println("Bandwith used   : " + user.getBandwidthUsed());
        System.out.println("Max Filesize    : " + user.getFilesizeMax());
        System.out.println("Icon Server     : " + user.getIconServer());
        System.out.println("Id              : " + user.getId());
        System.out.println("Location        : " + user.getLocation());
        System.out.println("Photos count    : " + user.getPhotosCount());
        System.out.println("Online status   : " + user.getOnline());
        System.out.println("Pics first date : " + user.getPhotosFirstDate());
        System.out.println("Pics first taken: " + user.getPhotosFirstDateTaken());
    }
    
    public boolean requestAuthorization(URL url)
    {
        System.out.println("Authentication URL: " + url);
        System.out.println("Visit the above URL and allow Read access");
        System.out.println("When access granted, press Enter to continue...");
        try
        {
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public void run()
    {
        FlickrUser user = null;
        
        try
        {
            flickrFactory.getLoginManager().login(this);
            user = flickrFactory.getLoginManager().getUser();
            if ( user == null )
            {
                // Cannot proceed
                System.out.println("User authorization is required. User cancelled request");
                return;
            }
            
            // printUserStats(user);
            System.out.println("Logged in as "+user.getUsername());            
            
            SyncBrowser browser = flickrFactory.getBrowser();
            SyncStore store = flickrFactory.getStore();
            
            List<Photoset> photosets = browser.getSets(user);
            
            System.out.println("Sets of user "+user.getUsername());
            listSets(photosets);
            
            System.out.println("Storing Etentje Rigouts");
            List<Photo> photos = browser.getPhotosInSet("1143644");
            store.savePhotos(photos, "EtentjeRigouts", null, null);
            
        }
        catch (FlickrAuthorizationRequiredException e)
        {
            e.printStackTrace();
        }
        catch (FlickrCommunicationException e)
        {
            e.printStackTrace();
        }
    }
    
    public void listSets(List<Photoset> photosets)
    {
        for (Photoset set : photosets)
        {
            System.out.println(set.getTitle()+" (id="+set.getId()+") - "+set.getPhotoCount());           
        }
    }
//    
//    public void listPhotosInSet(String setId) throws IOException, SAXException, FlickrException
//    {
//        PhotosetsInterface sets = flickr.getPhotosetsInterface();
//        PhotosInterface photoI = flickr.getPhotosInterface();
//        Collection photoCollection = sets.getPhotos(setId);
//        Iterator it = photoCollection.iterator();
//        while ( it.hasNext() )
//        {
//            Photo photo = (Photo) it.next();
//            photo = photoI.getInfo(photo.getId(), null);                
//            System.out.println("Photo: "+photo.getTitle()+" ("+photo.getId()+") - "+photo.getOriginalUrl());
//        }
//    }
    
}
