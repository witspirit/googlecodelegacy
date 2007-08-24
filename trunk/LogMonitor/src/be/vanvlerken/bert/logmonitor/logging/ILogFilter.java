/**
 * @author wItspirit
 * 5-feb-2003
 * ILogFilter.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * Interface for a filter on the logging
 */
public interface ILogFilter
{
    /**
     * Tells if the provided entry should be logged
     * @param entry
     * @return boolean
     */
    public boolean isAllowed(ILogEntry entry);
}
