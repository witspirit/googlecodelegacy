/**
 * @author wItspirit
 * 1-nov-2003
 * ProxyMonitorDecoder.java
 */

package be.vanvlerken.bert.logmonitor.logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This decoder provides backwards compatibility with the ProxyMonitor
 * It just parses the Sequence number, LogLevel and message
 * No interpretation is done of the timeStamp or of the module
 */
public class ProxyMonitorDecoder implements ILogEntryDecoder
{
    public ProxyMonitorDecoder()
    {
    }
    
    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryDecoder#decodeLogEntry(java.lang.String)
     */
    public ILogEntry decodeLogEntry(String encodedLogEntry)
    {
        long  seqNr;
        int logLevel;
        long  timeStamp;
        String moduleName;
        String workingStr;
        
        Matcher matcher;
        Pattern pattern;
        
        seqNr = 0;
        logLevel = ILogEntry.UNKNOWN;
        timeStamp = 0;
        workingStr = encodedLogEntry;
        moduleName = "ProxyMon";

        
        /* Extract the sequence number */
        pattern = Pattern.compile("[^:]+:");
        matcher = pattern.matcher(workingStr);
        
        if ( matcher.find() )
        {
            String temp = matcher.group();
            temp = temp.substring(0, temp.length()-1); /* Pinch of the : */
            seqNr = Long.parseLong(temp);
            workingStr = matcher.replaceFirst("");
        }
        else
        {
            /* If it's already screwed up when we are here, we probably better don't 
             * proceed with any further parsing. Just revert to NullCoder like behavior
             */
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
        }
        
        /* Extract the log level */
        pattern = Pattern.compile("[^:]+:");
        matcher = pattern.matcher(workingStr);
        
        if ( matcher.find() )
        {
            String temp = matcher.group();
            temp = temp.substring(0,temp.length()-1); /* Pinch of the : */
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

            workingStr = matcher.replaceFirst("");            
                        
        }
        else
        {
            /* If it's already screwed up when we are here, we probably better don't 
             * proceed with any further parsing. Just revert to NullCoder like behavior
             */
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
        }
        
        return new LogEntry(seqNr, logLevel, timeStamp, moduleName, workingStr);
    }

}
