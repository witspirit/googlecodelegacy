/**
 * @author wItspirit
 * 26-mrt-2005
 * ExportStatusReport.java
 */

package be.vanvlerken.bert.zfpricemgt.database.exporters;

/**
 * This interface forms the two-way communication between the export controller and
 * the export executor
 * The export controller is the export manager
 * The export executor is the database
 * The database will report its progress via this interface.
 * On the other hand, the export manager (ExportAction) will indicate if the export needs to
 * be cancelled or not. 
 */
public interface ExportStatusReport
{
    public static final double UNKNOWN_PROGRESS = -1.00;
    /**
     * Indicates whether the export was cancelled by the user.
     * @return 
     *      true    The export was cancelled by the user
     *      false   The export should continue
     */
    public abstract boolean isCancelled();
    
    /**
     * Sets the current progress as a value between 0.00 and 100.00 (percentage)
     * If progress is unknown, it should be set to UNKNOWN_PROGRESS
     * @param progress
     *          A value between 0.00 and 100.00 indicating a percentage of completion
     *          UNKNOWN_PROGRESS if the current progress cannot be assessed
     */
    public abstract void setProgress(double progress);    
}
