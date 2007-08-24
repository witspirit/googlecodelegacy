/**
 * @author wItspirit
 * 5-feb-2003
 * LogEntry.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * Basic iplementation of a LogEntry
 */
public class LogEntry implements ILogEntry
{
    private long sequenceNr;
    private int identifier;
    private long timeStamp;
    private String moduleName;
    private String message;
    
    public LogEntry(long seqNr, int id, long timeStamp, String moduleName, String message)
    {
        this.sequenceNr = seqNr;
        this.identifier = id;
        this.message = message;
        
        this.timeStamp = timeStamp;
        this.moduleName = moduleName;
    }

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogEntry#getSequenceNr()
	 */
	public long getSequenceNr()
	{
		return sequenceNr;
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogEntry#getIdentifier()
	 */
	public int getIdentifier()
	{
		return identifier;
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogEntry#getTimeStamp()
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogEntry#getModuleName()
	 */
	public String getModuleName()
	{
		return moduleName;
	}

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogEntry#getMessage()
	 */
	public String getMessage()
	{
		return message;
	}
}
