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
public class PhotoMetadata {
    private String photoId;
    private String title;
    private String description;
    private Owner owner;
    private String license;
    private Collection<String> tags;
    private Visibility visibility;
    private Collection<String> notes;
    private boolean isFavorite;
    private boolean isPrimary;

    @SuppressWarnings("unchecked")
    public PhotoMetadata(Photo photo) {
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

    public String getPhotoId() {
        return photoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getLicense() {
        return license;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Collection<String> getNotes() {
        return notes;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isPrimary() {
        return isPrimary;
    }
    
    
}
