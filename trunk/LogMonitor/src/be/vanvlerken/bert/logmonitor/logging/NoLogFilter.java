/**
 * @author wItspirit
 * 5-feb-2003
 * NoLogFilter.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * A filter that does not perform any filtering
 * In other words, it will allow ALL entries
 */
public class NoLogFilter implements ILogFilter
{

	/**
	 * @see be.vanvlerken.bert.logmonitor.ILogFilter#isAllowed(be.vanvlerken.bert.logmonitor.ILogEntry)
	 */
	public boolean isAllowed(ILogEntry entry)
	{
		return true;
	}

}
