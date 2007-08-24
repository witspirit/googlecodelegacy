/**
 * @author wItspirit
 * 16-mei-2005
 * Statistic.java
 */

package be.vanvlerken.bert.zfpricemgt.monitoring;

import java.util.LinkedList;
import java.util.List;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * This is a Value class that will store some properties of a certain statistic 
 */
public class Statistic extends StandardMBean implements StatisticMBean
{
    private String name;
    private int invocationCount;
    private List<Long> responseTimes;
    
    public Statistic(String name) throws NotCompliantMBeanException
    {
        super(StatisticMBean.class);

        this.name = name;
        invocationCount = 0;
        responseTimes = new LinkedList<Long>();
    }

    public int getInvocationCount()
    {
        return invocationCount;
    }
    
    public void increaseInvocationCount()
    {
        invocationCount++;
    }
    
    public void resetInvocationCount()
    {
        invocationCount = 0;
    }
    
    public String getName()
    {
        return name;
    }

    public void addResponseTime(long responseTime)
    {
        responseTimes.add(responseTime);
    }
    
    public long[] getResponseTimes()
    {
        long[] respTimes = new long[responseTimes.size()];
        int counter=0;
        for ( Long respTime : responseTimes )
        {
            respTimes[counter++] = respTime;
        }
        return respTimes;
    }
    
}
