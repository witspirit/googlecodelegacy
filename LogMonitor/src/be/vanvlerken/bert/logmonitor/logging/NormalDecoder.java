/**
 * @author wItspirit
 * 29-okt-2003
 * NormalDecoder.java
 */

package be.vanvlerken.bert.logmonitor.logging;

/**
 * Provides a basic decoder that reads all fields in a simple format
 */
public class NormalDecoder implements ILogEntryDecoder
{
    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryDecoder#decodeLogEntry(java.lang.String)
     */
    public ILogEntry decodeLogEntry(String encodedLogEntry)
    {
        long seqNr;
        int logLevel;
        long timeStamp;
        String module;
        String message;
        
        String workingStr = new String(encodedLogEntry);
        String temp;
        int end;
                
        /* Extract seqNr */
        end = workingStr.indexOf('|');
        
        if ( end != -1 )
        {
            temp = workingStr.substring(0, end);
            
            seqNr = Long.parseLong(temp);
            
            workingStr = workingStr.substring(end+1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
        }
        
        /* Extract logLevel */
        end = workingStr.indexOf('|');
        
        if ( end != -1 )
        {
            temp = workingStr.substring(0, end);
            
            if ( temp.equals("ERROR") )
            {
                logLevel = ILogEntry.ERROR;
            }
            else if ( temp.equals("WARNING") )
            {
                logLevel = ILogEntry.WARNING;
            }
            else if ( temp.equals("INFO") )
            {
                logLevel = ILogEntry.INFO;
            }
            else if ( temp.equals("VERBOSE") )
            {
                logLevel = ILogEntry.VERBOSE;
            }
            else
            {
                logLevel = ILogEntry.UNKNOWN;
            }
            
            workingStr = workingStr.substring(end+1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
        }

        /* Extract timeStamp */
        end = workingStr.indexOf('|');
        
        if ( end != -1 )
        {
            temp = workingStr.substring(0, end);
            
            timeStamp = Long.parseLong(temp);
            
            workingStr = workingStr.substring(end+1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
        }

        /* Extract module */
        end = workingStr.indexOf('|');
        
        if ( end != -1 )
        {
            temp = workingStr.substring(0, end);
            
            module = temp;
            
            workingStr = workingStr.substring(end+1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
        }
        
        /* Extract message */
        message = workingStr;
                
        return new LogEntry(seqNr, logLevel, timeStamp, module, message);
    }

}
