/**
 * @author wItspirit
 * 29-okt-2005
 * PhotosetDescription.java
 */

package be.vanvlerken.bert.flickrstore.gui.grouppoolselector;

import javax.swing.*;
import java.net.URL;
import java.util.Observable;

public class GroupPoolDescription extends Observable {
    public static final int UNKNOWN = -1;

    private int photoCount;
    private String name;
    private String description;
    private Icon icon;

    public GroupPoolDescription() {
        URL imageUrl = this.getClass().getClassLoader().getResource("resources/FlickrIcon.png");
        icon = new ImageIcon(imageUrl);

        name = "No GroupPool selected yet";
        description = "";
        photoCount = UNKNOWN;
    }

    public void updateDescription(Icon icon, String name, String description, int photoCount) {
        if (icon != null) {
            this.icon = icon;
        }
        this.name = name;
        this.description = description;
        this.photoCount = photoCount;

        this.setChanged();
        this.notifyObservers();
    }

    public Icon getIcon() {
        return icon;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
