/**
 * @author wItspirit
 * 29-okt-2003
 * NormalEncoder.java
 */

package be.vanvlerken.bert.logmonitor.logging;

/**
 * Provides a basic encoder that writes out all fields of a logEntry in a simple format
 */
public class NormalEncoder implements ILogEntryEncoder
{
    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryEncoder#encodeLogEntry(be.vanvlerken.bert.logmonitor.logging.ILogEntry)
     */
    public String encodeLogEntry(ILogEntry logEntry)
    {
        String seqNr;
        String logLevel;
        String timeStamp;
        String module;
        String message;
        
        seqNr = Long.toString(logEntry.getSequenceNr());
        switch ( logEntry.getIdentifier() )
        {
            case ILogEntry.ERROR: logLevel = "ERROR"; break;
            case ILogEntry.WARNING: logLevel = "WARNING"; break;
            case ILogEntry.INFO: logLevel = "INFO"; break;
            case ILogEntry.VERBOSE: logLevel = "VERBOSE"; break;
            case ILogEntry.UNKNOWN: logLevel = "UNKNOWN"; break;
            default: logLevel = "UNKNOWN";
        }
        timeStamp = Long.toString(logEntry.getTimeStamp());
        module = logEntry.getModuleName();
        message = logEntry.getMessage();
        
        return seqNr+"|"+logLevel+"|"+timeStamp+"|"+module+"|"+message;
    }

}
