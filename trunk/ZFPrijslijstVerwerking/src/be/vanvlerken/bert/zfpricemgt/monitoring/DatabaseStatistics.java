/**
 * @author wItspirit
 * 16-mei-2005
 * DatabaseStatistics.java
 */

package be.vanvlerken.bert.zfpricemgt.monitoring;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.management.NotCompliantMBeanException;

import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;

/**
 * This is an interception class that will count method invocations
 */
public class DatabaseStatistics
{
    private Statistic[]            statistics;
    private Map<Method, Statistic> statsMapper;

    public DatabaseStatistics()
    {
        try
        {
            statsMapper = new HashMap<Method, Statistic>();
            Method[] methods = IZFPriceDatabase.class.getMethods();
            for (Method method : methods)
            {                
                statsMapper.put(method, new Statistic(IZFPriceDatabase.class.getName() + "." + method.getName()));
            }
        }
        catch (NotCompliantMBeanException e)
        {
            e.printStackTrace();
        }

        statistics = new Statistic[statsMapper.values().size()];
        int counter = 0;
        for (Statistic stat : statsMapper.values())
        {
            statistics[counter++] = stat;
        }
    }

    public IZFPriceDatabase getDatabaseStatisticsProxy(IZFPriceDatabase target)
    {
        StatisticsHandler statHandler = new StatisticsHandler(target, statsMapper);
        IZFPriceDatabase dbProxy = (IZFPriceDatabase) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class< ? >[] { IZFPriceDatabase.class}, statHandler);
        return dbProxy;
    }

    public Statistic[] getStatistics()
    {
        return statistics;
    }

}
