/**
 * @author wItspirit
 * 24-mrt-2005
 * Exporter.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

import java.io.File;

import be.vanvlerken.bert.zfpricemgt.IProduct;

/**
 * The generic interface of an Exporter. 
 * The role of an Exporter is to export a price database or part of it to a file
 * In addition to adhering to this interface an implementation should have a no arguments
 * constructor. 
 * The startExport and stopExport will be called by the ExportManager
 * The export method will be called by an IZFPriceDatabase implementation
 */
public interface Exporter
{
    /**
     * Starts an export operation. 
     * The implementation can perform initialisation in this method.
     * If startExport is invoked twice, without a stopExport, the behaviour is undeterminate.
     * @param outputFile A writable output file
     */
    public abstract void startExport(File outputFile);
    
    /**
     * Writes the product to the currently set output file.
     * If no output file is set (startExport was not called), this method will do nothing
     * @param product The product to export
     */
    public abstract void export(IProduct product);
    
    /**
     *  Stops an export operation.
     *  The implementation should close any file handles it is using and make sure that
     *  all output is flushed to disk. 
     *  After a stopExport, the exporter should be capable of starting another export, using
     *  startExport. 
     */
    public abstract void stopExport();
    
    /**
     * Should be a user-friendly string stating which Exporter this is and what
     * kind of data it will write.
     * This string will be used by users to select from multiple candidates which
     * one to use.
     * @return
     */
    public abstract String toString();
}
