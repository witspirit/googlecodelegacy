/**
 * @author wItspirit
 * 16-nov-2003
 * CompositeFilter.java
 */

package be.vanvlerken.bert.logmonitor.logging;

import java.util.ArrayList;
import java.util.List;


/**
 * This filter allows to stack a set of filters
 */
public class CompositeFilter implements ILogFilter
{
    private List    filters;
    
    public CompositeFilter()
    {
        filters = new ArrayList();
    }
    
    public void addFilter(ILogFilter filter)
    {
        filters.add(filter);
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.ILogFilter#isAllowed(be.vanvlerken.bert.logmonitor.logging.ILogEntry)
     */
    public boolean isAllowed(ILogEntry entry)
    {
        boolean allowed = true;
        
        int i=0;
        while ( allowed && i < filters.size() )
        {
            ILogFilter filter = (ILogFilter) filters.get(i);
            allowed = filter.isAllowed(entry);
            i++; 
        }
        
        return allowed;
    }

}
