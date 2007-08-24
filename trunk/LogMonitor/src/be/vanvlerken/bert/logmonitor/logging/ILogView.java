/**
 * @author wItspirit
 * 5-feb-2003
 * ILogView.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * Provides a view on a certain LogDatabase
 */
public interface ILogView
{
    /**
     * Obtains a ILogEntry from the LogDatabase
     * @param index
     * @return ILogEntry
     */
    public ILogEntry get(int index);
    
    /**
     * Obtains the size (nr of entries) of the log database
     * @return int
     */
    public int getSize();
    
    /**
     * Register a IDatabaseListener to retrieve events when the database has changed
     * @param listener
     */
    public void addDatabaseListener(IDatabaseListener listener);
    
    /**
     * Activates the updating of this view
     */
    public void activate();
    
    /**
     * Deactivates the updating of this view
     */
    public void deactivate();
}
