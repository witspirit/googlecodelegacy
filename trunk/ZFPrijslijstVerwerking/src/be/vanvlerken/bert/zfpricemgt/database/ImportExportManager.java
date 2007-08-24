/**
 * @author wItspirit
 * 26-mrt-2005
 * ImportExportManager.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import be.vanvlerken.bert.zfpricemgt.database.exporters.ExportStatus;
import be.vanvlerken.bert.zfpricemgt.database.exporters.ExportTask;
import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;
import be.vanvlerken.bert.zfpricemgt.database.importers.ImportStatus;
import be.vanvlerken.bert.zfpricemgt.database.importers.ImportTask;
import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;


public class ImportExportManager
{
    private static ImportExportManager manager = null;
    
    private Exporter[] exporters;
    private Importer[] importers;
    private ExecutorService executor;

    public static ImportExportManager getInstance()
    {
        if ( manager == null )
        {
            manager = new ImportExportManager();
        }
        return manager;
    }
    
    private ImportExportManager()
    {
        executor = Executors.newSingleThreadExecutor();
        
        ClassPathResource importExportConfig = new ClassPathResource("be/vanvlerken/bert/zfpricemgt/database/importExportFactory.xml");
        BeanFactory importExportFactory = new XmlBeanFactory(importExportConfig);
        
        ImportExportFactory ieFactory = (ImportExportFactory) importExportFactory.getBean("ImportExportFactory");
        exporters = ieFactory.getExporters();
        importers = ieFactory.getImporters();
    }
    
    public Exporter[] getExporters()
    {
        return exporters;
    }
    
    public ExportStatus startExport(IZFPriceDatabase db, Exporter exporter, File outputFile, Date validSince)
    {
        ExportTask exportTask = new ExportTask(db, exporter, outputFile, validSince);
        executor.submit(exportTask);
        return exportTask;
    }
        
    public ImportStatus startImport(InputStream is, IZFPriceDatabase db, Date validSince, boolean overruleValidSince, Importer importer)
    {
        ImportTask importTask = new ImportTask(is, db, validSince, overruleValidSince, importer);
        executor.submit(importTask);
        return importTask;
    }

    /**
     * Presents the File to all registered importers for validation. All 
     * importers that claim to be able to parse the content will be returned. 
     * @return
     *      A List containing 0 or more importers that can parse the InputStream
     * @throws FileNotFoundException 
     */
    public List<Importer> getValidImporters(File inputFile) throws FileNotFoundException
    {
        // Tha API is like this, because InputStreams are soldom resettable
        // Notably: the FileInputStream is not resettable !
        List<Importer> validImporters = new LinkedList<Importer>();        
        for ( Importer importer: importers )
        {
            // Start with a fresh stream every time
            FileInputStream fis = new FileInputStream(inputFile);
            if ( importer.canImport(fis) )
            {
                validImporters.add(importer);
            }
            try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return validImporters;
    }
    
}
