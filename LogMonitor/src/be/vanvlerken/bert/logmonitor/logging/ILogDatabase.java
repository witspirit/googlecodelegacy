/**
 * @author wItspirit
 * 5-feb-2003
 * ILogDatabase.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * The general interface for accessing a Log Database
 */
public interface ILogDatabase extends ILogView
{
    /**
     * Adds a ILogEntry into the LogDatabase
     * @param entry
     */
    public void add(ILogEntry entry);
    
    /**
     * Clears all records from the database
     */
    public void clear();
    
    /**
     * Returns true when the database chops up entries based on newlines
     * As such it really displays multiline entries on multiple rows
     * They are identified, because they carry the same sequence number
     * @return
     */
    public boolean respectsNewlines();
    
    /**
     * Sets if newlines need to be respected by the database.
     * It will only affect NEW entries into the database and has
     * no affect on entries already entered.
     * @param respect
     */
    public void setRespectNewlines(boolean respect);
}
