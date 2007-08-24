/**
 * @author wItspirit
 * 12-mei-2003
 * TrafficEntityFacade.java
 */

package be.vanvlerken.bert.packetdistributor.common.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;

/**
 * Provides an Application facade for PacketDistributorServer classes
 */
public class Endpoint implements ITrafficEndpoint
{    
    private String IP;
    private int    Port;
    private String Protocol;

    public Endpoint()
    {
        IP = null;
        Port = 0;
        Protocol = null;
    }

    public Endpoint(String ipAddress, int port, String protocol)
    {
        this.IP = ipAddress;
        this.Port = port;
        this.Protocol = protocol;
    }

    /**
     * @return
     */
    public InetAddress getIpAddress()
    {
        try
        {
            return InetAddress.getByName(IP);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return
     */
    public int getPort()
    {
        return Port;
    }

    /**
     * @return
     */
    public String getProtocolStr()
    {
        return Protocol;
    }

    public int getProtocol()
    {
        if (Protocol.equalsIgnoreCase("UDP"))
        {
            return ITrafficEndpoint.UDP;
        }
        else if (Protocol.equalsIgnoreCase("TCP"))
        {
            return ITrafficEndpoint.UDP;
        }
        else
        {
            return ITrafficEndpoint.UNKNOWN;
        }
    }

}
