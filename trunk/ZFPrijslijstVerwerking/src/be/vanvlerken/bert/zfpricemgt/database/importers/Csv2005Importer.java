/**
 * @author wItspirit
 * 4-feb-2005
 * Csv2005Importer.java
 */

package be.vanvlerken.bert.zfpricemgt.database.importers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.Product;

/**
 * This importer is capable of reading the 2005 CSV format provided by ZF
 */
public class Csv2005Importer implements Importer
{
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.importers.localization.Csv2005Importer");
    private static final Log log = LogFactory.getLog(Csv2005Importer.class);
    
    private BufferedReader reader;
    private Date validSince;

    private final Pattern parsePattern;
    private final Pattern detectPattern;

    public Csv2005Importer()
    {
        //More 'flexible' pattern -> allows for quotes and whitespace to be ignored
        detectPattern = Pattern.compile("^\\s*\"?\\s*.{10}\\s*\"?\\s*;\\s*\"?\\s*[^\";]*\\s*\"?\\s*;\\s*\"?\\s*\\d*\\.?\\d*\\s*\"?\\s*;\\s*\"?\\s*[^\",]*\"?\\s*$");
        parsePattern = Pattern.compile("^\\s*\"?\\s*(.{4})(.{3})(.{3})\\s*\"?\\s*;\\s*\"?\\s*([^\"]*)\\s*\"?\\s*;\\s*\"?\\s*(\\d*\\.?\\d*)\\s*\"?\\s*;\\s*\"?\\s*[^\",]*\"?\\s*$");        
    }
    
    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.importers.Importer#canImport(java.io.InputStream)
     */
    public boolean canImport(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        try
        {
            line = reader.readLine();
        }
        catch (IOException e)
        {
            return false;
        }

        return detectPattern.matcher(line).matches();
    }

    public String toString()
    {
        return msgs.getString("description");
    }

    public void startImport(InputStream is, Date validSince, boolean overruleValidSince)
    {
        reader = new BufferedReader(new InputStreamReader(is));
        this.validSince = validSince;
    }

    public IProduct nextProduct()
    {
        try
        {
            String line = reader.readLine();
            // Try until we find something parseable or the end of the stream
            while (line != null)
            {
                Matcher matcher = parsePattern.matcher(line);
                if (matcher.find())
                {
                    String productId = matcher.group(1)+" "+matcher.group(2)+" "+matcher.group(3);
                    String productDescription = matcher.group(4);
                    double price = Double.parseDouble(matcher.group(5));
                    
                    return new Product(productId, productDescription, price, validSince);
                }
                else
                {
                    log.warn("Could not parse: " + line);
                }

                // Read the next line
                line = reader.readLine();
            }
            return null;
        }
        catch (IOException e)
        {
            // Ignore...
            // e.printStackTrace();
            return null;
        }
    }

    public void stopImport()
    {
        reader = null;
        validSince = null;       
    }
}
