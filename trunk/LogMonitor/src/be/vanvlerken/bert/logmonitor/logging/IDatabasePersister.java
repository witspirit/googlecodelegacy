/**
 * @author wItspirit
 * 1-nov-2003
 * IDatabasePersister.java
 */

package be.vanvlerken.bert.logmonitor.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Interface for database persistency
 */
public interface IDatabasePersister
{
    public void loadDb(File file) throws FileNotFoundException, IOException;
    public void save() throws FileNotFoundException, IOException;
    public void saveAs(File file) throws IOException;
}
