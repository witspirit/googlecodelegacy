/**
 * @author wItspirit
 * 26-mrt-2005
 * ExportTask.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;


public class ExportTask implements ExportStatus, ExportStatusReport, Runnable
{
    private static final Log log = LogFactory.getLog(ExportTask.class);
    
    private double progress;
    private boolean cancel;
    private boolean done;

    private IZFPriceDatabase db;
    private Exporter exporter;
    private File outputFile;
    private Date validSince;
    
    public ExportTask(IZFPriceDatabase db, Exporter exporter, File outputFile, Date validSince)
    {
        this.db = db;
        this.exporter = exporter;
        this.outputFile = outputFile;
        this.validSince = validSince;
        
        progress = ExportStatus.UNKNOWN_PROGRESS;
        cancel = false;    
        done = false;
    }
    
    public synchronized double getProgress()
    {
        return progress;
    }

    public synchronized boolean isDone()
    {
        return done;
    }

    public synchronized void cancel()
    {
        cancel = true;
    }

    public synchronized boolean isCancelled()
    {
        return cancel;
    }

    public synchronized void setProgress(double progress)
    {
        this.progress = progress;
    }

    public void run()
    {
        log.debug("Preparing export to "+outputFile+" ...");
        exporter.startExport(outputFile);
        log.debug("Exporting...");
        db.exportProducts(this, exporter, validSince);
        log.debug("Finishing export");
        exporter.stopExport();
        log.debug("Export finished.");
        synchronized (this)
        {
            done = true;
        }
    }

}
