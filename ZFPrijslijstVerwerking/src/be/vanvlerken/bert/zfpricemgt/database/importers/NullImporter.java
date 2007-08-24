/**
 * @author wItspirit
 * 2-feb-2005
 * NullImporter.java
 */

package be.vanvlerken.bert.zfpricemgt.database.importers;

import java.io.InputStream;
import java.util.Date;
import java.util.ResourceBundle;

import be.vanvlerken.bert.zfpricemgt.IProduct;


/**
 * This importer does absolutely nothing.
 */
public class NullImporter implements Importer
{
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.importers.localization.NullImporter");
    
    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.importers.Importer#canImport(java.io.InputStream)
     */
    public boolean canImport(InputStream is)
    {
        return true;
    }

    public String toString()
    {
        return msgs.getString("description");
    }

    public void startImport(InputStream is, Date validSince, boolean overruleValidSince)
    {
    }

    public IProduct nextProduct()
    {
        return null;
    }

    public void stopImport()
    {
    }
}
