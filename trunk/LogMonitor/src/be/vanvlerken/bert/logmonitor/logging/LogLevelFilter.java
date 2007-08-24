/**
 * @author wItspirit
 * 16-nov-2003
 * LogLevelFilter.java
 */

package be.vanvlerken.bert.logmonitor.logging;

/**
 * This is a filter that simply filters based on log levels
 * It's immutable
 */
public class LogLevelFilter implements ILogFilter
{
    private boolean logErrors;
    private boolean logWarnings;
    private boolean logInfo;
    private boolean logVerbose;

    public LogLevelFilter(boolean logErrors, boolean logWarnings, boolean logInfo, boolean logVerbose)
    {
        this.logErrors = logErrors;
        this.logWarnings = logWarnings;
        this.logInfo = logInfo;
        this.logVerbose = logVerbose;
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogFilter#isAllowed(be.vanvlerken.bert.logmonitor.logging.ILogEntry)
     */
    public boolean isAllowed(ILogEntry entry)
    {
        boolean allowed = false;

        switch (entry.getIdentifier())
        {
            case LogEntry.ERROR :
                allowed = logErrors;
                break;
            case LogEntry.WARNING :
                allowed = logWarnings;
                break;
            case LogEntry.INFO :
                allowed = logInfo;
                break;
            case LogEntry.VERBOSE :
                allowed = logVerbose;
                break;
            default :
                allowed = true;
        }

        return allowed;
    }

}
