/*
 * @author wItspirit 4-mei-2004 Log4JDecoder.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * A Decoder for a specific configuration of Log4J
 * .layout=org.apache.log4j.PatternLayout
 * .layout.ConversionPattern=%p|%d{ISO8601}|[%t] %c|%m%n
 */
public class Log4JDecoder implements ILogEntryDecoder
{
    static long seqCounter = 0;
    
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
        
        boolean printParsing = false;
        
        /* Extract seqNr */
        seqNr = seqCounter++;

        /* Extract logLevel */
        end = workingStr.indexOf('|');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);
            
            if ( printParsing )
                System.out.println("Extracting loglevel: "+temp);

            if (temp.equals("ERROR") || temp.equals("FATAL") )
            {
                logLevel = ILogEntry.ERROR;
            }
            else if (temp.equals("WARN"))
            {
                logLevel = ILogEntry.WARNING;
            }
            else if (temp.equals("INFO"))
            {
                logLevel = ILogEntry.INFO;
            }
            else if (temp.equals("DEBUG"))
            {
                logLevel = ILogEntry.VERBOSE;
            }
            else
            {
                logLevel = ILogEntry.UNKNOWN;
            }

            workingStr = workingStr.substring(end + 1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule",
                    encodedLogEntry);             
        }

        /* Extract timeStamp */
        end = workingStr.indexOf('|');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);

            if ( printParsing )
                System.out.println("Extracting timeStamp: "+temp);

            // timeStamp = Long.parseLong(temp);
            // A more complex extraction is required, as the
            // timestamp is received in ISO8601 format
            timeStamp = parseDateStr(temp);

            workingStr = workingStr.substring(end + 1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule",
                    encodedLogEntry);             
        }

        /* Extract module */
        end = workingStr.indexOf('|');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);

            if ( printParsing )
                System.out.println("Extracting module: "+temp);

            module = temp;

            workingStr = workingStr.substring(end + 1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule",
                    encodedLogEntry);             
        }

        /* Extract message */
        message = workingStr;
        
        return new LogEntry(seqNr, logLevel, timeStamp, module, message);
    }
    
    private long parseDateStr(String temp)
    {
        long timeStamp;
        String work = new String(temp);

        DateFormat dateFormat =
            DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM,
                new Locale("pl"));
        /* Testprogram revealed that there is no matching format for the
         * dateStr we receive.
         * Should do some preprocessing on the dateStr to get it into some known format
         */
         
        /* Transformation of the input has to be performed:
         * Just remove the trailing ,123
         */
        work = (work.split(","))[0];

        Date timestampDate;
        try
        {
            timestampDate = dateFormat.parse(work);
            timeStamp = timestampDate.getTime();
        }
        catch (ParseException e)
        {
            // System.out.println("Current time according to dateFormat: "+dateFormat.format(new Date()));
            timeStamp = 0;
        }
        return timeStamp;
    }

}