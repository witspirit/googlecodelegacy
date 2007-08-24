/**
 * @author wItspirit
 * 29-okt-2003
 * NullDecoder.java
 */

package be.vanvlerken.bert.logmonitor.logging;


/**
 * This provides an implementation of the ILogEntryDecoder interface that does no 
 * decoding whatsoever. It just preserves the message as is.
 */
public class NullDecoder implements ILogEntryDecoder
{
    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryDecoder#decodeLogEntry(java.lang.String)
     */
    public ILogEntry decodeLogEntry(String encodedLogEntry)
    {
        return new LogEntry(0, ILogEntry.VERBOSE, 0, "unknownModule", encodedLogEntry);
    }

}
