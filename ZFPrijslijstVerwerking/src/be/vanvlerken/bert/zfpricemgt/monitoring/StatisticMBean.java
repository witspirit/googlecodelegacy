/**
 * @author wItspirit
 * 16-mei-2005
 * StatisticMBean.java
 */

package be.vanvlerken.bert.zfpricemgt.monitoring;


public interface StatisticMBean
{
    public abstract int getInvocationCount();
    public abstract String getName();
    public abstract long[] getResponseTimes();
    
    public abstract void resetInvocationCount();
}
