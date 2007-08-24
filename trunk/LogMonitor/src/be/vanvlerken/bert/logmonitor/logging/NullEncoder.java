/**
 * @author wItspirit
 * 29-okt-2003
 * NullEncoder.java
 */

package be.vanvlerken.bert.logmonitor.logging;


/**
 * Provides an implementation of the ILogEntryEncoder interface that actually does not perform
 * any encoding whatsoever. It only outputs the message part of the logEntry.
 */
public class NullEncoder implements ILogEntryEncoder
{
    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryEncoder#encodeLogEntry(be.vanvlerken.bert.logmonitor.logging.ILogEntry)
     */
    public String encodeLogEntry(ILogEntry logEntry)
    {
        return logEntry.getMessage();
    }

}
