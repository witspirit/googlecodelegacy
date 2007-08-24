/**
 * @author wItspirit
 * 9-okt-2005
 * PhotoMetadata.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.util.Collection;

import com.aetrion.flickr.photos.Photo;


/**
 * Stores photo metadata
 */
public class PhotoMetadata
{
    private String photoId;
    private String title;
    private String description;
    private Owner owner;
    private String license;
    private Collection tags;
    private Visibility visibility;
    private Collection notes;
    private boolean isFavorite;
    private boolean isPrimary;
    
    public PhotoMetadata(Photo photo)
    {
        photoId = photo.getId();
        title = photo.getTitle();
        description = photo.getDescription();
        owner = new Owner(photo.getOwner());
        license = photo.getLicense();
        tags = photo.getTags();
        visibility = new Visibility(photo.isFamilyFlag(), photo.isFriendFlag(), photo.isPublicFlag());
        notes = photo.getNotes();
        isFavorite = photo.isFavorite();
        isPrimary = photo.isPrimary();
    }
}
