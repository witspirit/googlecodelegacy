/*
 * @author witspirit
 * 13-jun-2003
 * PdDirectInterface.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.direct;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;

/**
 * Entry point to the PacketDistributorServer functionality
 * An instance of this should be delivered to any interface attached to it.
 * This mechanism allows for multiple interfaces attached to a single instance
 * AND
 * it allows for every interface to have it's own instance
 * EVEN MORE
 * it allows to use a certain interface for multiple instances
 */
public class PdDirectInterface implements IPacketDistributorAPI
{
    private Map<Integer, ITrafficRelay> relays;
    
    public PdDirectInterface()
    {
        relays = new HashMap<Integer, ITrafficRelay>();
    }
    
	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#getRelays()
	 */
	public Collection<ITrafficRelay> getRelays()
	{
        return relays.values();
	}

    /**
     * Method createRelay.
     * @return ITrafficRelay
     */
    public ITrafficRelay createRelay(String name)
    {
        int id = (int) (Math.random() * 65535);
        while ( relays.containsKey(new Integer(id)) )
        {
            id = (int) (Math.random() * 65535);
        }
        
        ITrafficRelay relay = new TrafficRelayImp(id, name);
        relays.put(id, relay);
        
        return relay;
    }



	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#createTrafficSource(java.net.InetAddress, int, int)
	 */
	public ITrafficSource createTrafficSource(ITrafficEndpoint endpoint
		)
	{
        ITrafficSource trafficSource;
        
        try
        {
            switch ( endpoint.getProtocol() )
            {
                case ITrafficEndpoint.UDP: trafficSource = new UdpTrafficSource(new InetSocketAddress(endpoint.getIpAddress(), endpoint.getPort())); break;
                case ITrafficEndpoint.TCP: trafficSource = new TcpTrafficSource(new InetSocketAddress(endpoint.getIpAddress(), endpoint.getPort())); break;
                default: trafficSource = null;
            }
        }
        catch (IOException e)
        {
            trafficSource = null;
            e.printStackTrace();
        }
		return trafficSource;
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#createTrafficDestination(java.net.InetAddress, int, int)
	 */
	public ITrafficDestination createTrafficDestination(ITrafficEndpoint endpoint
		)
	{
        ITrafficDestination trafficDestination;
        
        try
        {
            switch ( endpoint.getProtocol() )
            {
                case ITrafficEndpoint.UDP: trafficDestination = new UdpTrafficDestination(new InetSocketAddress(endpoint.getIpAddress(), endpoint.getPort())); break;
                case ITrafficEndpoint.TCP: trafficDestination = new TcpTrafficDestination(new InetSocketAddress(endpoint.getIpAddress(), endpoint.getPort())); break;
                default: trafficDestination = null;
            }
        }
        catch ( SocketException se )
        {
            trafficDestination = null;
            se.printStackTrace();
        }
        return trafficDestination;
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#destroyRelay(be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay)
	 */
	public void destroyRelay(ITrafficRelay relay)
	{
        relay.cleanUp();
        relays.remove(relay.getId());
	}

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI#shutdown()
     */
    public void shutdown()
    {
        for (ITrafficRelay relay : getRelays())
        {
            relay.cleanUp();
        }
        relays.clear();
    }
}
