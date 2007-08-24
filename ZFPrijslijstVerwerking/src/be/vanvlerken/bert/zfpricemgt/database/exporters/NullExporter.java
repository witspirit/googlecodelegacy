/**
 * @author wItspirit
 * 24-mrt-2005
 * NullExporter.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

import java.io.File;
import java.util.ResourceBundle;

import be.vanvlerken.bert.zfpricemgt.IProduct;

/**
 * Implementation of the Exporter interface that does absolutely nothing 
 */
public class NullExporter implements Exporter
{
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.exporters.localization.NullExporter");
    
    public void startExport(File outputFile)
    {
    }

    public void export(IProduct product)
    {
    }

    public void stopExport()
    {
    }

    @Override
    public String toString()
    {
        return msgs.getString("description");
    }

}
