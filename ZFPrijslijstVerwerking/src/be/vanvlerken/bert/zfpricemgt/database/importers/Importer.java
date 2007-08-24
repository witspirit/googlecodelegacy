/**
 * @author wItspirit
 * 2-feb-2005
 * Importer.java
 */

package be.vanvlerken.bert.zfpricemgt.database.importers;

import java.io.InputStream;
import java.util.Date;

import be.vanvlerken.bert.zfpricemgt.IProduct;


/**
 * This is a generic interface for an importer
 * In addition to this, the Importer should have a no-argument constructor.
 */
public interface Importer
{
    /**
     * This method should try to verify if the input format is something that can be
     * be imported by this importer. 
     * @param is
     *          The InputStream containing the serialised records
     * @return
     *      true if the input stream can be imported by this importer
     *      false if the input stream can not be imported by this importer
     */
    public abstract boolean canImport(InputStream is);
    
    /**
     * Starts an import operation. 
     * The implementation can perform initialisation in this method.
     * If startImport is invoked twice, without a stopImport, the behaviour is undeterminate.
     * @param is
     *          The InputStream to read the records from 
     * @param validSince
     *          The date from which these records are valid. 
     * @param overruleValidSince
     *          If this is true, all records should be set with the provided validSince date
     *          If this is false, the importer can choose wether or not to use the validSince date provided 
     */
    public abstract void startImport(InputStream is, Date validSince, boolean overruleValidSince);
    
    /**
     * Fetches a new product from the InputStream
     * @return
     *      The next product in the InputStream.
     *      null if there were no more products to be read
     */
    public abstract IProduct nextProduct();
    
    /**
     *  Stops an import operation.
     *  The implementation should clean up and free all resources
     *  After a stopImpport, the importer should be capable of starting another import, using
     *  startImport. 
     */
    public abstract void stopImport();
    
    /**
     * Should be a user-friendly string stating which Importer this is and what
     * kind of data it can read.
     * This string will be used by users to select from multiple candidates which
     * one to use.
     * @return
     */
    public abstract String toString();
    
}
