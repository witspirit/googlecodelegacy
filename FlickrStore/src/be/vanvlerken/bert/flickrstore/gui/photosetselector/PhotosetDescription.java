/**
 * @author wItspirit
 * 29-okt-2005
 * PhotosetDescription.java
 */

package be.vanvlerken.bert.flickrstore.gui.photosetselector;

import javax.swing.*;
import java.net.URL;
import java.util.Observable;

public class PhotosetDescription extends Observable {
    public static final int UNKNOWN = -1;

    private Icon icon;
    private int photocount;
    private String title;
    private String description;

    public PhotosetDescription() {
        URL imageUrl = this.getClass().getClassLoader().getResource("resources/FlickrIcon.png");
        icon = new ImageIcon(imageUrl);

        title = "No PhotoSet selected yet";
        description = null;
        photocount = UNKNOWN;
    }

    public void updateDescription(Icon icon, String title, String description, int photocount) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.photocount = photocount;

        this.setChanged();
        this.notifyObservers();
    }

    public Icon getIcon() {
        return icon;
    }

    public int getPhotocount() {
        return photocount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
