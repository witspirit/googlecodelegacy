/**
 * @author wItspirit
 * 24-mrt-2005
 * LogExporter.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.IProduct;

/**
 * Does not actually export the data, but just logs the output 
 */
public class LogExporter implements Exporter
{
    private static final Log log = LogFactory.getLog(LogExporter.class);
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.exporters.localization.LogExporter");
    
    public void startExport(File outputFile)
    {
        log.info("startExport - "+outputFile);
    }

    public void export(IProduct product)
    {
        log.info("export - "+product);
    }

    public void stopExport()
    {
        log.info("stopExport");
    }

    @Override
    public String toString()
    {
        return msgs.getString("description");
    }

}
