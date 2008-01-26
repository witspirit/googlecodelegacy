/**
 * @author wItspirit
 * 11-nov-2005
 * OverwriteNever.java
 */

package be.vanvlerken.bert.flickrstore.store;

import java.io.File;

public class OverwriteNever implements OverwriteStrategy {
    public boolean overwrite(File file) {
	return false;
    }

}
