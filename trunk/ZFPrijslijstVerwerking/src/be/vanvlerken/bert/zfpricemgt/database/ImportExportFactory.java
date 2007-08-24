/**
 * @author wItspirit
 * 15-mei-2005
 * ImportExportFactory.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;
import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;


/**
 * This class is responsible for managing the importers and exporters 
 */
public class ImportExportFactory
{
    private Importer[] importers;
    private Exporter[] exporters;
    
    public ImportExportFactory(Importer[] importers, Exporter[] exporters)
    {
        this.importers = importers;
        this.exporters = exporters;
    }
    
    public Exporter[] getExporters()
    {
        return exporters;
    }
    
    public Importer[] getImporters()
    {
        return importers;
    }
}
