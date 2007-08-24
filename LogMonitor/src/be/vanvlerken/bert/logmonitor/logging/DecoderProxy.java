/**
 * @author wItspirit
 * 1-nov-2003
 * DecoderProxy.java
 */

package be.vanvlerken.bert.logmonitor.logging;


/**
 * This class functions as a proxy for a real ILogEntryDecoder implementation
 * Basically it allows for changing the real implementation during the lifetime of the proxy
 * In this way the ILogEntryDecoder interface is immutable and straightforward and we can 
 * provide mutability through this proxy.
 */
public class DecoderProxy implements ILogEntryDecoder
{
    private ILogEntryDecoder    decoder;
    
    public DecoderProxy(ILogEntryDecoder decoder)
    {
        this.decoder = decoder;
    }
    
    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogEntryDecoder#decodeLogEntry(java.lang.String)
     */
    public ILogEntry decodeLogEntry(String encodedLogEntry)
    {
        return decoder.decodeLogEntry(encodedLogEntry);
    }

    /**
     * @return ILogEntryDecoder The real decoder in use at the moment
     */
    public ILogEntryDecoder getDecoder()
    {
        return decoder;
    }

    /**
     * Set's the new decoder implementation, active immediately
     * @param decoder
     */
    public void setDecoder(ILogEntryDecoder decoder)
    {
        this.decoder = decoder;
    }
}
