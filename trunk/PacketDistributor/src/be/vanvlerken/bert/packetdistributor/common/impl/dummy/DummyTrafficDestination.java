/*
 * @author witspirit
 * 16-jun-2003
 * DummyTrafficDestination.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.dummy;

import java.net.InetAddress;

import be.vanvlerken.bert.packetdistributor.common.DataChunk;
import be.vanvlerken.bert.packetdistributor.common.DeliveryException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;

/**
 * Dummy implementation of an ITrafficDestination
 */
public class DummyTrafficDestination implements ITrafficDestination
{
    private ITrafficEndpoint endpoint;
    
    public DummyTrafficDestination(ITrafficEndpoint endpoint)
    {
        this.endpoint = endpoint;
    }

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#send(be.vanvlerken.bert.packetdistributor.ui.console.generics.DataChunk)
	 */
	public void send(DataChunk data) throws DeliveryException
	{
        // Do nothing
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#getIpAddress()
	 */
	public InetAddress getIpAddress()
	{
		return endpoint.getIpAddress();
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#getPort()
	 */
	public int getPort()
	{
		return endpoint.getPort();
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#getProtocol()
	 */
	public int getProtocol()
	{
		return endpoint.getProtocol();
	}
}
