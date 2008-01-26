/**
 * @author wItspirit
 * 11-nov-2005
 * OverwriteAlways.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.io.File;

public class OverwriteAlways implements OverwriteStrategy {
    public boolean overwrite(File file) {
	return true;
    }
}
