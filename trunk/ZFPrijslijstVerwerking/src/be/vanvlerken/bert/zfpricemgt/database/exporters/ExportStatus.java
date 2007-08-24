/**
 * @author wItspirit
 * 25-mrt-2005
 * ExportStatus.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

/**
 * Allows observation and interaction with an Export task
 * Implementations are expected run the export task in a seperate thread
 */
public interface ExportStatus
{
    public static final double UNKNOWN_PROGRESS = -1.00;

    /**
     * Gives the progress of the export as a percentage between 0.00 and 100.00
     * Return UNKNOWN_PROGRESS if no progress information is available
     * @return
     *      A value between 0.00 and 100.00 indicating progress of the export
     *      UNKNOWN_PROGRESS if no progress information is available
     */
    public abstract double getProgress();
    
    /**
     * Reports true when the export operation has completed succesfully
     * @return
     *      true when export has completed
     *      false when the export is still ongoing
     */
    public abstract boolean isDone();
    
    /**
     * Cancel the ongoing export.
     */
    public abstract void cancel();
}
