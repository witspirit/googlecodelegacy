/**
 * @author wItspirit
 * 28-dec-2004
 * ConfigurationDump.java
 */

package be.vanvlerken.bert.packetdistributor.common.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;


/**
 * This class will just dump all configuration of a passed in PacketDistributorImpl
 * to the logging console.
 */
public class ConfigurationDump
{
    public static final Log log = LogFactory.getLog(ConfigurationDump.class);
    
    public static void dumpConfiguration(IPacketDistributorAPI pd)
    {
        for ( ITrafficRelay relay : pd.getRelays() )
        {
            log.debug("Relay: id="+relay.getId()+" name="+relay.getName());
            log.debug("  Sources: ");
            for ( ITrafficSource source : relay.getSources() )
            {
                dumpEndpoint(source);
            }
            log.debug("  Destinations: ");
            for ( ITrafficDestination destination : relay.getDestinations() )
            {
                dumpEndpoint(destination);
            }
        }
    }

    /**
     * @param endpoint
     */
    private static void dumpEndpoint(ITrafficEndpoint endpoint)
    {        
        log.debug("    "+endpoint.getIpAddress().getHostAddress()+":"+(endpoint.getProtocol() == ITrafficEndpoint.UDP ? "UDP" : "TCP")+":"+endpoint.getPort());
    }
}
