/**
 * @author wItspirit
 * 5-feb-2003
 * IFilteredLogView.java
 */
package be.vanvlerken.bert.logmonitor.logging;

/**
 * Extends the ILogView to support Filters
 */
public interface IFilteredLogView extends ILogView
{
    /**
     * Set a filter on this view
     * @param logFilter
     */
    public void setFilter(ILogFilter logFilter);
    
    /**
     * Obtain the filter that is used on this view
     * @return ILogFilter
     */
    public ILogFilter getFilter();
}
