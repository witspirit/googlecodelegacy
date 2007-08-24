/**
 * @author wItspirit
 * 23-apr-2003
 * FileOperationsInterface.java
 */
package be.vanvlerken.bert.components.gui.fileselector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Defines the interface the basic FileOperation Actions require
 */
public interface FileOperationsInterface
{
    public void open(File file) throws FileNotFoundException, IOException;
    public void save() throws FileNotFoundException, IOException;
    public void saveAs(File file) throws FileNotFoundException, IOException;
}
