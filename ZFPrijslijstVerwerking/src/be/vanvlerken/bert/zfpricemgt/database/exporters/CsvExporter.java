/**
 * @author wItspirit
 * 24-mrt-2005
 * CsvExporter.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.IProduct;

/**
 * Exports the database to a Comma Seperated Values file 
 */
public class CsvExporter implements Exporter
{
    private static final Log log = LogFactory.getLog(CsvExporter.class);
    private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.exporters.localization.CsvExporter");
   
    private PrintWriter printer = null;
    
    
    public void startExport(File outputFile)
    {
        try
        {
            printer = new PrintWriter(outputFile);
        }
        catch (FileNotFoundException e)
        {
            log.error("Could not start export.", e);
        }
    }

    public void export(IProduct product)
    {
        if ( printer == null ) return;
        
        printer.println(product.getNumber()+","+product.getDescription()+","+product.getPrice()+","+df.format(product.getValidSince()));
    }

    public void stopExport()
    {
        printer.flush();
        printer.close();
        printer = null;
    }

    @Override
    public String toString()
    {
        return msgs.getString("description");
    }

}
