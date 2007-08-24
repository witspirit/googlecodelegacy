/**
 * @author vlerkenb
 * 25-dec-2004
 * PacketDistributorServer.java
 */
package be.vanvlerken.bert.packetdistributor.ui.console;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.configuration.PacketDistributorFactory;

/**
 * This application provides sort of NAT/PAT like functionality It provides an
 * interface to set to which port and which protocol it should listen to and
 * relays that to one or more destinations.
 */
public class PacketDistributor implements Runnable
{
    final static private Log         log = LogFactory
                                                 .getLog(PacketDistributor.class);
    private PacketDistributorFactory factory;

    public static void main(String[] args)
    {
        ArgumentInterpreter argParser = new ArgumentInterpreter("packetdistributor.xml");
        if (!argParser.parse(args))
        {
            argParser.printUsage();
            System.exit(1);
        }
        String configFile = argParser.getConfigXml();
        PacketDistributor pd = new PacketDistributor(configFile);
        pd.run();
    }

    /**
     * @param configFile
     */
    public PacketDistributor(String configFile)
    {
        factory = new PacketDistributorFactory(configFile);
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        IPacketDistributorAPI api = factory.getPacketDistributorAPI();
        try
        {
            System.out.println("Press any key to stop PacketDistributorConsole");
            System.in.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        api.shutdown();
    }

}
