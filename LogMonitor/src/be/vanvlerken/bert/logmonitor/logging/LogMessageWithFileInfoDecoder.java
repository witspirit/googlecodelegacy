/*
 * @author wItspirit
 * 26-okt-2003
 * LogMessageDecoder.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Encoder/Decoder translates the format outputted by the
 * FrameProcessor to the LoggingDatabase format
 */
public class LogMessageWithFileInfoDecoder implements ILogEntryDecoder
{
    public LogMessageWithFileInfoDecoder()
    {
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryDecoder#decodeLogEntry(java.lang.String)
     */
    public ILogEntry decodeLogEntry(String encodedLogEntry)
    {
        long seqNr;
        int logLevel;
        long timeStamp;
        String moduleName;
        String workingStr;

        Matcher matcher;
        Pattern pattern;

        seqNr = 0;
        logLevel = ILogEntry.UNKNOWN;
        timeStamp = 0;
        workingStr = encodedLogEntry;
        moduleName = "unknownModule";

        /* Extract the sequence number */
        pattern = Pattern.compile("[^:]+:");
        matcher = pattern.matcher(workingStr);

        if (matcher.find())
        {
            String temp = matcher.group();
            temp = temp.substring(0, temp.length() - 1); /* Pinch of the : */
            seqNr = Long.parseLong(temp);
            workingStr = matcher.replaceFirst("");
        }
        else
        {
            /* If it's already screwed up when we are here, we probably better don't 
             * proceed with any further parsing. Just revert to NullCoder like behavior
             */
            return new LogEntry(
                0,
                ILogEntry.VERBOSE,
                0,
                "unknownModule",
                encodedLogEntry);
        }

        /* Extract the log level */
        pattern = Pattern.compile("[^:]+:");
        matcher = pattern.matcher(workingStr);

        if (matcher.find())
        {
            String temp = matcher.group();
            temp = temp.substring(0, temp.length() - 1); /* Ping of the : */
            if (temp.equals("ERROR"))
            {
                logLevel = ILogEntry.ERROR;
            }
            else if (temp.equals("WARNING"))
            {
                logLevel = ILogEntry.WARNING;
            }
            else if (temp.equals("INFO"))
            {
                logLevel = ILogEntry.INFO;
            }
            else if (temp.equals("VERBOSE"))
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
            return new LogEntry(
                0,
                ILogEntry.VERBOSE,
                0,
                "unknownModule",
                encodedLogEntry);
        }

        /* Extract the Date */
        pattern = Pattern.compile("^.{24}");
        matcher = pattern.matcher(workingStr);

        if (matcher.find())
        {
            String temp = matcher.group();

            timeStamp = parseDateStr(temp);

            workingStr = matcher.replaceFirst("");
        }

        /* Extract the module name */
        pattern = Pattern.compile("^[^>]+>");
        matcher = pattern.matcher(workingStr);

        if (matcher.find())
        {
            moduleName = matcher.group();
            moduleName = moduleName.substring(0, moduleName.length() - 1);
            /* Pinch of the > */
            workingStr = matcher.replaceFirst("");
        }
        else
        {
            /* If it's already screwed up when we are here, we probably better don't 
             * proceed with any further parsing. Just revert to NullCoder like behavior
             */
            return new LogEntry(
                0,
                ILogEntry.VERBOSE,
                0,
                "unknownModule",
                encodedLogEntry);
        }

        /* Extract the filename (short form) */
        pattern = Pattern.compile("[^,]*,");
        matcher = pattern.matcher(workingStr);

        if (matcher.find())
        {
            String temp = matcher.group();

            Pattern iPat = Pattern.compile("/[^\\./]*\\.[^\\.,/]{1,3}");
            Matcher iMatcher = iPat.matcher(temp);

            if (iMatcher.find())
            {
                temp = iMatcher.group();
                temp = temp.substring(1); /* Pinch of the leading / */
                moduleName = moduleName + "[" + temp;
            }

            workingStr = matcher.replaceFirst("");

            /* Extract the lineNr */
            pattern = Pattern.compile("[^,]+,");
            matcher = pattern.matcher(workingStr);

            if (matcher.find())
            {
                temp = matcher.group();

                iPat = Pattern.compile("\\d+");
                iMatcher = iPat.matcher(temp);

                if (iMatcher.find())
                {
                    temp = iMatcher.group();
                    moduleName = moduleName + ":" + temp + "]";
                }
                else
                {
                    moduleName = moduleName + "]";
                }

                workingStr = matcher.replaceFirst("");
            }
            else
            {
                moduleName = moduleName + "]";
            }
        }

        /* Extract the ErrorCode */
        pattern = Pattern.compile("[^,]+,");
        matcher = pattern.matcher(workingStr);

        if (matcher.find())
        {
            String temp = matcher.group();

            Pattern iPattern = Pattern.compile("\\d+");
            Matcher iMatcher = iPattern.matcher(temp);

            if (iMatcher.find())
            {
                temp = iMatcher.group();

                if (!temp.equals("0"))
                {
                    workingStr =
                        matcher.replaceFirst("ErrorCode=" + temp + " - ");
                }
                else
                {
                    workingStr = matcher.replaceFirst("");
                }
            }
            else
            {
                workingStr = matcher.replaceFirst("");
            }
        }

        return new LogEntry(seqNr, logLevel, timeStamp, moduleName, workingStr);
    }

    private long parseDateStr(String temp)
    {
        long timeStamp;
        String work = new String(temp);

        DateFormat dateFormat =
            DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM,
                Locale.UK);
        /* Testprogram revealed that there is no matching format for the
         * dateStr we receive.
         * Should do some preprocessing on the dateStr to get it into some known format
         */
         
        /* Transformation of the input has to be performed:
         * Original: Sun Oct 26 17:47:01 2003
         * Target  : 26-Oct-2003 17:47:01
         */
        work = work.substring(4);
        // System.out.println(work);
        
        String month = work.substring(0,3);
        String date = work.substring(4,6);
        String hour = work.substring(7,15);
        String year = work.substring(16);
        
        // System.out.println("Month="+month+" Date="+date+"Hour="+hour+"Year="+year);
        work = date+"-"+month+"-"+year+" "+hour;
        // System.out.println(work);
         
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
