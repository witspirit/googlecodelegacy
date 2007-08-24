/**
 * @author wItspirit
 * 28-dec-2004
 * MgtConfigurationDump.java
 */

package be.vanvlerken.bert.packetdistributor.common.impl.rmi;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;

/**
 * This class will just dump all configuration of a passed in
 * PacketDistributorImpl to the logging console.
 */
public class MgtConfigurationDump
{
    public static final Log log = LogFactory.getLog(MgtConfigurationDump.class);

    public static void dumpConfiguration(IRemotePacketDistributorAPI pd)
    {
        try
        {
            for (IRemoteTrafficRelay relay : pd.getRelays())
            {
                log.debug("Relay: id=" + relay.getId() + " name="
                        + relay.getName());
                log.debug("  Sources: ");
                for (ITrafficEndpoint source : relay.getSources())
                {
                    dumpEndpoint(source);
                }
                log.debug("  Destinations: ");
                for (ITrafficEndpoint destination : relay
                        .getDestinations())
                {
                    dumpEndpoint(destination);
                }
            }
        }
        catch (RemoteException e)
        {
            log.warn("Could not properly obtain configuration information: "
                    + e.getMessage());
        }
    }

    /**
     * @param endpoint
     */
    private static void dumpEndpoint(ITrafficEndpoint endpoint)
    {
        log.debug("    "
                + endpoint.getIpAddress().getHostAddress()
                + ":"
                + (endpoint.getProtocol() == ITrafficEndpoint.UDP ? "UDP"
                        : "TCP") + ":" + endpoint.getPort());
    }
}
