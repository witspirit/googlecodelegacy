/**
 * @author wItspirit
 * 5-feb-2003
 * IDatabaseListener.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * Interface to receive notification of changes in the database
 */
public interface IDatabaseListener
{
    /**
     * Triggered when a new entry has been added to the database
     * @param entry
     */
    public void entryAdded(ILogEntry entry);
    
    /**
     * Triggered when one or more entries changed
     * @param startIndex
     * @param endIndex
     */
    public void entriesModified(int startIndex, int endIndex);
    
    /**
     * Triggered when the size or the entries drastically change
     * All entries should be considered invalid, as the size
     */
    public void databaseModified();
}
