/**
 * @author wItspirit
 * 2-feb-2005
 * ImportTask.java
 */

package be.vanvlerken.bert.zfpricemgt.database.importers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;

/**
 * This is an implementation of the ImportStatus interface
 */
public class ImportTask implements ImportStatus, Runnable
{
    private InputStream is;
    private IZFPriceDatabase db;
    private Importer importer;
    private Date validSince;
    private boolean overruleValidSince;    
    private boolean done;
    
    /**
     * 
     */
    public ImportTask(InputStream is, IZFPriceDatabase db, Date validSince, boolean overruleValidSince, Importer importer)
    {
        this.is = is;
        this.db = db;
        this.validSince = validSince;
        this.overruleValidSince = overruleValidSince;
        this.importer = importer;
        done = false;
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.importers.ImportStatus#isDone()
     */
    public synchronized boolean isDone()
    {
        return done;
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.importers.ImportStatus#cancel()
     */
    public void cancel()
    {
        try
        {
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        importer.startImport(is, validSince, overruleValidSince);
        db.importProducts(importer);
        importer.stopImport();
        synchronized (this)
        {
            done = true;
        }
    }
}
