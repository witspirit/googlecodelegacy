/**
 * @author wItspirit
 * 2-feb-2005
 * ImportAction.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.Timer;

import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;
import be.vanvlerken.bert.zfpricemgt.database.ImportExportManager;
import be.vanvlerken.bert.zfpricemgt.database.importers.ImportStatus;
import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;

/**
 * Performs the Import operation
 */
public class ImportAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ImportAction");
    
    private JFrame                     parent;
    private IZFPriceDatabase           db;
    private ImportExportManager        importManager;

    /**
     * 
     */
    public ImportAction(JFrame parent, IZFPriceDatabase db)
    {
        super(msgs.getString("title"));
        this.parent = parent;
        this.db = db;
        importManager = ImportExportManager.getInstance();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        ImportDialog importDialog = new ImportDialog(parent);
        int dialogStatus = importDialog.showImportDialog();
        if ( dialogStatus == ImportDialog.IMPORT )
        {
            try
            {
                InputStream inputStream = new FileInputStream(importDialog.getSelectedFile());
                ProgressMonitorInputStream progressInputStream = new ProgressMonitorInputStream(parent, msgs.getString("progress.title"), inputStream);
                List<Importer> validImporters = importManager.getValidImporters(importDialog.getSelectedFile());
                Importer importer = null;
                if ( validImporters.size() == 1 )
                {
                    importer = validImporters.get(0);
                }
                else if ( validImporters.size() > 1 ) 
                {
                    ImporterSelectorDialog selector= new ImporterSelectorDialog(parent);
                    importer = selector.selectImporter(validImporters);
                }
                if ( importer != null ) // Protects also against problems with the selector dialog
                {
                    ImportStatus importStatus = importManager.startImport(progressInputStream, db, importDialog.getValidSince(), importDialog.overruleDate(), importer);
                    new TimerListener(progressInputStream, importStatus);                                        
                }
                else
                {
                    JOptionPane.showMessageDialog(parent, msgs.getString("no.importers.found.for.format"), null, JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (FileNotFoundException e)
            {
                JOptionPane.showMessageDialog(parent, e.getLocalizedMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class TimerListener implements ActionListener
    {
        private final Timer timer;
        private final ProgressMonitorInputStream progressInputStream;
        private final ImportStatus importStatus;
        
        public TimerListener(ProgressMonitorInputStream progressInputStream, ImportStatus importStatus)
        {
            timer = new Timer(500, this);
            this.progressInputStream = progressInputStream;
            this.importStatus = importStatus;
            timer.start();
        }
        
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae)
        {
            try
            {
                if (progressInputStream.getProgressMonitor().isCanceled())
                {
                    importStatus.cancel();
                    progressInputStream.close();
                    timer.stop();
                }
                else if (importStatus.isDone())
                {
                    progressInputStream.close();
                    timer.stop();
                    JOptionPane.showMessageDialog(parent, msgs.getString("import.completed"));
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
