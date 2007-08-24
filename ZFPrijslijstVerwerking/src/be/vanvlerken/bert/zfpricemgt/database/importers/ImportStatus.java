/**
 * @author wItspirit
 * 2-feb-2005
 * ImportStatus.java
 */

package be.vanvlerken.bert.zfpricemgt.database.importers;


/**
 * Reports on the status of an Import operation performed by the ImportEngine
 */
public interface ImportStatus
{
    /**
     * Reports true when the import operation has completed succesfully
     * @return
     *      true when import has completed
     *      false when the import is still ongoing
     */
    public abstract boolean isDone();
    
    /**
     * Cancel the ongoing import.
     * BEWARE: There is no rollback ! All records already inserted, remain inserted !
     */
    public abstract void cancel();
}
