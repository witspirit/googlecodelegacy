/**
 * @author wItspirit
 * 5-feb-2003
 * LogView.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.util.Vector;

/**
 * Provides a flexible implementation of a stackable FilteredLogView
 */
public class LogView extends AbstractLogView
	implements IFilteredLogView, IFilterUpdate, IDatabaseListener
{
    private ILogView    database;
    private ILogFilter  filter;

    private Vector              visibleLogs;
    private boolean            realTimeLogging;
    private long                lastSeqNr;

    public LogView(ILogView database)
    {
        this.database = database;
        this.database.addDatabaseListener(this);
        filter = new NoLogFilter();
        realTimeLogging = true;
        lastSeqNr = 0;
        
        readVisibleLogs();
    }
    
    public LogView(ILogView database, ILogFilter logFilter)
    {
        this.database = database;
        this.database.addDatabaseListener(this);
        filter = logFilter;
        visibleLogs = new Vector();
        lastSeqNr = 0;
        
        readVisibleLogs();
    }
	
    /**
	 * Method readVisibleLogs.
	 */
	private void readVisibleLogs()
	{
        visibleLogs = new Vector();
                
        for (int i=0; i < database.getSize(); i++)
        {
            ILogEntry entry;
            
            entry = database.get(i);
            if ( filter.isAllowed(entry) )
            {
                visibleLogs.add(entry);
            }
            
            if ( !realTimeLogging
                  && 
                  entry.getSequenceNr() >= lastSeqNr )
            {
                break;
            }
        }
	}
    
	/**
	 * @see be.vanvlerken.bert.logmonitor.IFilteredLogView#setFilter(be.vanvlerken.bert.logmonitor.ILogFilter)
	 */
	public void setFilter(ILogFilter logFilter)
	{
        filter = logFilter;
        
        readVisibleLogs();
        fireDatabaseModified();
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.IFilteredLogView#getFilter()
	 */
	public ILogFilter getFilter()
	{
		return filter;
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.IFilterUpdate#filterUpdated()
	 */
	public void filterUpdated()
	{
        readVisibleLogs();
        fireDatabaseModified();
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.IDatabaseListener#entryAdded(be.vanvlerken.bert.logmonitor.ILogEntry)
	 */
	public void entryAdded(ILogEntry entry)
	{
        if ( realTimeLogging && filter.isAllowed(entry))
        {
            visibleLogs.add(entry);
            fireEntryAdded(entry);
        }
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.IDatabaseListener#entriesModified(int, int)
	 */
	public void entriesModified(int startIndex, int endIndex)
	{
        /* Ignore */
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.IDatabaseListener#databaseModified()
	 */
	public void databaseModified()
	{
        if ( realTimeLogging )
        {
            readVisibleLogs();
            fireDatabaseModified();
        }
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogView#get(int)
	 */
	public ILogEntry get(int index)
	{
		return (ILogEntry) visibleLogs.get(index);
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogView#getSize()
	 */
	public int getSize()
	{
		return visibleLogs.size();
	}
    
    public void setRealTime(boolean realTime)
    {
        realTimeLogging = realTime;
                
        if ( realTime )
        {
            databaseModified();
        }
        else
        {
            if ( visibleLogs.isEmpty() )
            {
                lastSeqNr = 0;
            }
            else
            {
                lastSeqNr = ((LogEntry) visibleLogs.lastElement()).getSequenceNr();
            }
        }
    }
    
    public boolean getRealtime()
    {
        return realTimeLogging;
    }
}
