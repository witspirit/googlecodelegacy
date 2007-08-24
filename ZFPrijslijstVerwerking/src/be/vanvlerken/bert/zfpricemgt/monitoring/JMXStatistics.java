/**
 * @author wItspirit
 * 16-mei-2005
 * Statistics.java
 */

package be.vanvlerken.bert.zfpricemgt.monitoring;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class JMXStatistics
{
    private Statistic[] stats;

    public JMXStatistics(Statistic[] stats) throws NotCompliantMBeanException
    {
        this.stats = stats;

        registerMBeans();
    }

    private void registerMBeans()
    {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try
        {
            for (Statistic stat : stats)
            {
                ObjectName statName = new ObjectName("bean:type=Statistics, name=" + stat.getName());
                server.registerMBean(stat, statName);
            }
        }
        catch (MalformedObjectNameException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (InstanceAlreadyExistsException e)
        {
            e.printStackTrace();
        }
        catch (MBeanRegistrationException e)
        {
            e.printStackTrace();
        }
        catch (NotCompliantMBeanException e)
        {
            e.printStackTrace();
        }
    }
}
