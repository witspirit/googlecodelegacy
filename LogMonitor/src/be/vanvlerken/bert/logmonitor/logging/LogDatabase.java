/**
 * @author wItspirit
 * 5-feb-2003
 * LogDatabase.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the LogDatabase
 */
public class LogDatabase extends AbstractLogView implements ILogDatabase
{
    private List          logDb;
    private boolean       respectNewlines;

    public LogDatabase(boolean respectNewlines)
    {
        logDb = new ArrayList();
        this.respectNewlines = respectNewlines;
    }

    /* ***************** LOG DATABASE INTERFACE ********************** */
    /**
     * @see be.vanvlerken.bert.logmonitor.ILogDatabase#add(be.vanvlerken.bert.logmonitor.ILogEntry)
     */
    public void add(ILogEntry entry)
    {
        if (respectNewlines)
        {
            String[] messageParts = entry.getMessage().split("\n");
            for (int i=0; i < messageParts.length; i++)
            {
                LogEntry logEntry = new LogEntry(entry.getSequenceNr(), entry.getIdentifier(), entry.getTimeStamp(), entry.getModuleName(), messageParts[i]);
                logDb.add(logEntry);
                fireEntryAdded(logEntry); // Possibly painful...
            }
        }
        else
        {
            logDb.add(entry);
            fireEntryAdded(entry);
        }
    }

    /**
     * Clears all records from the database
     */
    public void clear()
    {
        logDb.clear();
        fireDatabaseModified();
        System.gc();
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.ILogView#get(int)
     */
    public ILogEntry get(int index)
    {
        return (ILogEntry) logDb.get(index);
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.ILogView#getSize()
     */
    public int getSize()
    {
        return logDb.size();
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogDatabase#respectsNewlines()
     */
    public boolean respectsNewlines()
    {
        return respectNewlines;
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogDatabase#setRespectNewlines(boolean)
     */
    public void setRespectNewlines(boolean respect)
    {
        respectNewlines = respect;
    }

    /* ************************************************************** */

}