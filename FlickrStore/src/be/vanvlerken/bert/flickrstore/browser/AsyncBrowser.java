/**
 * @author wItspirit
 * 28-okt-2005
 * AsyncBrowser.java
 */

package be.vanvlerken.bert.flickrstore.browser;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.swing.Icon;

import be.vanvlerken.bert.flickrstore.login.FlickrUser;

import com.aetrion.flickr.groups.Group;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photosets.Photoset;


public class AsyncBrowser
{
    private SyncBrowser browser;
    private ExecutorService executor;
    
    public AsyncBrowser(ExecutorService executor, SyncBrowser browser)
    {
        this.executor = executor;
        this.browser = browser;
    }

    private class GetSets implements Callable<List<Photoset>>
    {
        private FlickrUser user;
        
        public GetSets(FlickrUser user)
        {
            this.user = user;
        }
        
        public List<Photoset> call() throws Exception
        {
            return browser.getSets(user);
        }        
    }
    
    private class GetPhotosInSet implements Callable<List<Photo>>
    {
        private String setId;
        
        public GetPhotosInSet(String setId)
        {
            this.setId = setId;
        }
        
        public List<Photo> call() throws Exception
        {
            return browser.getPhotosInSet(setId);
        }        
    }
    
    private class GetMiniPhoto implements Callable<Icon>
    {
        private Photo photo;
        
        public GetMiniPhoto(Photo photo)
        {
            this.photo = photo;
        }

        public Icon call() throws Exception
        {
            return browser.getMiniPhoto(photo);
        }
    }
    
    private class GetPhotoset implements Callable<Photoset>
    {
        private String setId;
        
        public GetPhotoset(String setId)
        {
            this.setId = setId;
        }
        
        public Photoset call() throws Exception
        {
            return browser.getPhotoset(setId);
        }        
    }
    
    private class GetGroup implements Callable<Group>
    {
        private String url;
        
        public GetGroup(String url)
        {
            this.url = url;
        }

        public Group call() throws Exception
        {
            return browser.getGroupPoolInfo(url);
        }
    }
    
    private class GetGroupPhotos implements Callable<List<Photo>>
    {
        private String groupId;
        
        public GetGroupPhotos(String groupId)
        {
            this.groupId = groupId;
        }
        
        public List<Photo> call() throws Exception
        {
            return browser.getGroupPhotos(groupId);
        }        
    }
    
    public Future<List<Photoset>> getSets(FlickrUser user)
    {
        return executor.submit(new GetSets(user));
    }

    public Future<List<Photo>> getPhotosInSet(String setId)
    {
        return executor.submit(new GetPhotosInSet(setId));
    }
    
    public Future<Photoset> getPhotoset(String setId)
    {
        return executor.submit(new GetPhotoset(setId));
    }

    public Future<Icon> getMiniPhoto(Photo photo)
    {
        return executor.submit(new GetMiniPhoto(photo));
    }
    
    public Future<Group> getGroup(String groupPoolUrl)
    {
        return executor.submit(new GetGroup(groupPoolUrl));
    }
    
    public Future<List<Photo>> getGroupPhotos(String groupId)
    {
        return executor.submit(new GetGroupPhotos(groupId));
    }
}
