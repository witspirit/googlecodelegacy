/**
 * @author wItspirit
 * 11-nov-2005
 * AskUserOverwriteStrategy.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import be.vanvlerken.bert.flickrstore.store.OverwriteStrategy;

import javax.swing.*;
import java.io.File;

public class AskUserOverwriteStrategy implements OverwriteStrategy {

    public boolean overwrite(File file) {
        int result = JOptionPane.showConfirmDialog(null, "Do you want to overwrite " + file.getName() + " ?", "Overwrite ?", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

}
