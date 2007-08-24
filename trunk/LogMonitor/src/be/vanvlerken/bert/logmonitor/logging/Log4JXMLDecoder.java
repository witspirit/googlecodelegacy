/*
 * @author wItspirit 4-mei-2004 Log4JDecoder.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * A Decoder for a specific configuration of Log4J
 * .layout=org.apache.log4j.xml.XMLLayout
 */
public class Log4JXMLDecoder implements ILogEntryDecoder
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

        boolean printParsing = false;

        /* Extract module (=logger) */
        end = workingStr.indexOf('"');
        workingStr = workingStr.substring(end + 1);
        end = workingStr.indexOf('"');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);

            if (printParsing)
                System.out.println("Extracting module: " + temp);

            module = temp;

            workingStr = workingStr.substring(end + 1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule",
                    encodedLogEntry);
        }

        /* Extract timeStamp */
        end = workingStr.indexOf('"');
        workingStr = workingStr.substring(end + 1);
        end = workingStr.indexOf('"');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);

            if (printParsing)
                System.out.println("Extracting timeStamp: " + temp);

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

        /* Extract seqNr */
        end = workingStr.indexOf('"');
        workingStr = workingStr.substring(end + 1);
        end = workingStr.indexOf('"');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);

            if (printParsing)
                System.out.println("Extracting sequenceNumber: " + temp);

            seqNr = Long.parseLong(temp);

            workingStr = workingStr.substring(end + 1);
        }
        else
        {
            return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule",
                    encodedLogEntry);
        }

        /* Extract logLevel */
        end = workingStr.indexOf('"');
        workingStr = workingStr.substring(end + 1);
        end = workingStr.indexOf('"');

        if (end != -1)
        {
            temp = workingStr.substring(0, end);

            if (printParsing)
                System.out.println("Extracting loglevel: " + temp);

            if (temp.equals("ERROR") || temp.equals("FATAL"))
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

        /* Extract message */
        end = workingStr.indexOf('[');
        workingStr = workingStr.substring(end + 1);
        end = workingStr.indexOf('[');
        workingStr = workingStr.substring(end + 1);
        end = workingStr.indexOf("]]>");
        
        message = workingStr.substring(0, end);        

        return new LogEntry(seqNr, logLevel, timeStamp, module, message);
    }

    private long parseDateStr(String temp)
    {
        return Long.parseLong(temp);
    }

}