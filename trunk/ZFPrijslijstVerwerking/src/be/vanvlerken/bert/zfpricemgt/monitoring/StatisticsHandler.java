/**
 * @author wItspirit
 * 16-mei-2005
 * StatisticsHandler.java
 */

package be.vanvlerken.bert.zfpricemgt.monitoring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


public class StatisticsHandler implements InvocationHandler
{
    private Object target;
    private Map<Method, Statistic> statsMapper;
    
    public StatisticsHandler(Object target, Map<Method, Statistic> statsMapper)
    {
        this.target = target;
        this.statsMapper = statsMapper;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object returnValue;
        long invokeTime;
        long returnTime;
        
        invokeTime = System.nanoTime();
        returnValue = method.invoke(target, args);
        returnTime = System.nanoTime();
        
        Statistic stat = statsMapper.get(method);
        stat.increaseInvocationCount();
        stat.addResponseTime(returnTime-invokeTime);
        
        return returnValue;
    }

}
