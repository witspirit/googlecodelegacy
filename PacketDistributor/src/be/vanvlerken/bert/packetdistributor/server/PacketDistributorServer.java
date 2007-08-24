/**
 * @author vlerkenb
 * 25-dec-2004
 * PacketDistributorServer.java
 */
package be.vanvlerken.bert.packetdistributor.server;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;

/**
 * This application provides sort of NAT/PAT like functionality It provides an
 * interface to set to which port and which protocol it should listen to and
 * relays that to one or more destinations.
 */
public class PacketDistributorServer implements Runnable
{
    final static private Log         log = LogFactory
                                                 .getLog(PacketDistributorServer.class);
    private XmlBeanFactory beanFactory;

    public static void main(String[] args)
    {
        ArgumentInterpreter argParser = new ArgumentInterpreter("be/vanvlerken/bert/packetdistributor/server/spring.xml");
        if (!argParser.parse(args))
        {
            argParser.printUsage();
            System.exit(1);
        }
        String configFile = argParser.getConfigXml();
        PacketDistributorServer pd = new PacketDistributorServer(configFile);
        pd.run();
    }

    /**
     * @param configFile
     */
    public PacketDistributorServer(String configFile)
    {        
        beanFactory = new XmlBeanFactory(new MixedResourceLoader().getResource(configFile));
        beanFactory.preInstantiateSingletons();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            System.out.println("Press any key to stop PacketDistributorServer");
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        IPacketDistributorAPI api = (IPacketDistributorAPI) beanFactory.getBean("PacketDistributorImpl");
        api.shutdown();
        beanFactory.destroySingletons();
        
        try
        {
            // Forced shutdown after a couple of seconds...
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }

}
