/**
 * @author wItspirit
 * 12-mei-2003
 * TrafficEntityFacade.java
 */

package be.vanvlerken.bert.packetdistributor.common;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Provides an Application facade for PacketDistributorServer classes
 */
public class TrafficEndpoint implements ITrafficEndpoint, Serializable
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3905523813025462584L;
    
    private InetAddress ipAddress;
    private int         port;
    private int         protocol;

    public TrafficEndpoint(InetAddress ipAddress, int port, int protocol)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.protocol = protocol;
    }
    
    public TrafficEndpoint(ITrafficEndpoint endpoint)
    {
        ipAddress = endpoint.getIpAddress();
        port = endpoint.getPort();
        protocol = endpoint.getProtocol();
    }
    
    /**
     * @return
     */
    public InetAddress getIpAddress()
    {
        return ipAddress;
    }

    /**
     * @return
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @return
     */
    public int getProtocol()
    {
        return protocol;
    }

}
