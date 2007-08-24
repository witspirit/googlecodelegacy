/**
 * @author wItspirit
 * 26-okt-2003
 * ILogEntryDecoder.java
 */

package be.vanvlerken.bert.logmonitor.logging;


/**
 * This interface defines a decode method to create an ILogEntry based
 * on a an encoded String
 */
public interface ILogEntryDecoder
{
    public ILogEntry decodeLogEntry(String encodedLogEntry);
}
