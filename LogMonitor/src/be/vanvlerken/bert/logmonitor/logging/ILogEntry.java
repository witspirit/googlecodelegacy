/**
 * @author wItspirit
 * 5-feb-2003
 * ILogEntry.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * An entry in the log database
 */
public interface ILogEntry
{
    /**
     * Some constants, representing the error levels
     */
    public static final int ERROR = 0;
    public static final int WARNING = 10;
    public static final int INFO = 20;
    public static final int VERBOSE = 30;
    
    /**
     * For an UNKNOWN type
     */ 
    public static final int UNKNOWN = 0xFFFF;
    
    /**
     * Gets the sequence number of this ILogEntry
     * @return int
     */
    public long getSequenceNr();
    
    /**
     * Returns an identifier for this log entry
     * Can be analyzed using the constants defined herein
     * @return int
     */
    public int getIdentifier();
    
    /**
     * Returns the time stamp of this log entry
     * @return long
     */
    public long getTimeStamp();
    
    /**
     * Returns the name of the module that created the entry
     * @return String
     */
    public String getModuleName();
    
    /**
     * Returns the log message
     * @return String
     */
    public String getMessage();

}
