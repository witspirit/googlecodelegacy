/**
 * @author wItspirit
 * 4-jan-2004
 * IZFPriceDatabase.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import java.util.Date;

import be.vanvlerken.bert.zfpricemgt.AlreadyExistsException;
import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.exporters.ExportStatusReport;
import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;
import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;


/**
 * Abstracts all interaction with the ZF Price Database
 */
public interface IZFPriceDatabase
{
    /**
     * This method should return an implementation of the DBConfigurator interface
     * @return
     *          An instance of the DBConfigurator.
     *          null If no configuration is required.
     */
    public abstract DBConfigurator getConfigurator();
    
    /**
     * Retrieves a the most recent information regarding the product
     * @param productNumber
     *      The product number of the product
     * @return
     *      The product matching your query, null if it didn't exist
     */
    public abstract IProduct getProduct(String productNumber);
    
    /**
     * Retrieves all information regarding the product. Thus if the product
     * as multiple historic price records, they will all be retrieved.
     * @param productNumber
     *          The product number for the product
     * @return
     *          An array containing all information regarding the product
     */
    public abstract IProduct[] getProducts(String productNumber);
    
    /**
     * Inserts a new product into the database
     * @param newProduct
     *          The new product to insert
     */
    public abstract void addProduct(IProduct newProduct) throws AlreadyExistsException;
    
    /**
     * Removes a specific product record from the database
     * @param product
     *          The product record to remove
     */
    public abstract void deleteProduct(IProduct product);

    /**
     * Exports either the complete database or a part of it
     * @param exporter The exporter that will transform the product into a file (not null)
     * @param validSince If null, the complete database will be exported. Otherwise the export will only contain prices that are at least valid since this date.  
     */
    public abstract void exportProducts(ExportStatusReport report, Exporter exporter, Date validSince);
    
    /**
     * Imports whatever product is delivered by the importer
     * @param importer
     *          An importer that will read records from some source and deliver them as IProduct objects
     */
    public abstract void importProducts(Importer importer);
}
