/**
 * @author vlerkenb
 * 25-dec-2004
 * PacketDistributorClient.java
 */
package be.vanvlerken.bert.packetdistributor.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import be.vanvlerken.bert.packetdistributor.ui.console.ArgumentInterpreter;

/**
 * This application provides sort of NAT/PAT like functionality It provides an
 * interface to set to which port and which protocol it should listen to and
 * relays that to one or more destinations.
 */
public class PacketDistributorClient implements Runnable
{
    final static private Log         log = LogFactory
                                                 .getLog(PacketDistributorClient.class);
    private XmlBeanFactory beanFactory;

    public static void main(String[] args)
    {
        ArgumentInterpreter argParser = new ArgumentInterpreter("be/vanvlerken/bert/packetdistributor/client/spring.xml");
        if (!argParser.parse(args))
        {
            argParser.printUsage();
            System.exit(1);
        }
        String configFile = argParser.getConfigXml();
        PacketDistributorClient pd = new PacketDistributorClient(configFile);
        pd.run();
    }

    /**
     * @param configFile
     */
    public PacketDistributorClient(String configFile)
    {        
        beanFactory = new XmlBeanFactory(new MixedResourceLoader().getResource(configFile));
        beanFactory.preInstantiateSingletons();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        // Do nothing special for now
        // Everything has been started in the beanFactory
    }

}
