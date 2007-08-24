/**
 * @author wItspirit
 * 23-mrt-2005
 * ExportAction.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;
import be.vanvlerken.bert.zfpricemgt.database.ImportExportManager;
import be.vanvlerken.bert.zfpricemgt.database.exporters.ExportStatus;
import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;

/**
 * Performs the Export operation
 */
public class ExportAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	
	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ExportAction");
    private static final Log log = LogFactory.getLog(ExportAction.class);

    private JFrame                      parent;
    private IZFPriceDatabase            db;
    private ImportExportManager         exportManager;

    /**
     * 
     */
    public ExportAction(JFrame parent, IZFPriceDatabase db)
    {
        super(msgs.getString("title"));
        this.parent = parent;
        this.db = db;
        exportManager = ImportExportManager.getInstance();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {        
        ExportDialog exportDialog = new ExportDialog(parent, exportManager.getExporters());
        int dialogStatus = exportDialog.showExportDialog();
        if (dialogStatus == ExportDialog.EXPORT)
        {
            // Perform the export
            File outputFile = exportDialog.getOutputFile();
            Exporter exporter = exportDialog.getExporter();
            Date validSince = null;
            if (!exportDialog.exportCompleteDatabase())
            {
                validSince = exportDialog.getValidSince();
            }
            ProgressMonitor progressMonitor = new ProgressMonitor(parent, msgs.getString("progress.title"), null, 0, 100);
            log.debug("Initiating Export");
            ExportStatus exportStatus = exportManager.startExport(db, exporter, outputFile, validSince);
            log.debug("Export initiated");            
            new TimerListener(exportStatus, progressMonitor);            
        }
    }

    private class TimerListener implements ActionListener
    {
        private final ExportStatus exportStatus;
        private final ProgressMonitor progressMonitor;
        private final Timer timer;
        
        public TimerListener(ExportStatus exportStatus, ProgressMonitor progressMonitor)
        {
            timer = new Timer(500,this);
            this.exportStatus = exportStatus;
            this.progressMonitor = progressMonitor;
            timer.start();
        }
        
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae)
        {
            log.debug("Timer event received");
            if (progressMonitor.isCanceled())
            {
                log.debug("Export is cancelled.");
                exportStatus.cancel();
                progressMonitor.close();
                timer.stop();
            }
            else if (exportStatus.isDone())
            {
                log.debug("Export is completed.");
                progressMonitor.close();
                timer.stop();
                JOptionPane.showMessageDialog(parent, msgs.getString("export.completed"));
            }
            else
            {
                double progressStatus = exportStatus.getProgress();
                log.debug("Update progress to "+progressStatus);                
                int progress = 0;
                if (progressStatus != ExportStatus.UNKNOWN_PROGRESS)
                {
                    progress = (int) Math.round(progressStatus);                    
                }
                progressMonitor.setProgress(progress);
            }
        }
    }

}
