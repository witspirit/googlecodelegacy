/*
 * @author witspirit
 * 16-jun-2003
 * DummyTrafficSource.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.dummy;

import java.net.InetAddress;

import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.TrafficListener;

/**
 * Dummy implementation of an ITrafficSource
 */
public class DummyTrafficSource implements ITrafficSource
{
    private ITrafficEndpoint endpoint;
    
    public DummyTrafficSource(ITrafficEndpoint endpoint)
    {
        this.endpoint = endpoint;
    }

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#addTrafficListener(be.vanvlerken.bert.packetdistributor.ui.console.generics.TrafficListener)
	 */
	public void addTrafficListener(TrafficListener trafficListener)
	{
        // Do nothing
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#startUp()
	 */
	public void startUp()
	{
        // Do nothing
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#cleanUp()
	 */
	public void cleanUp()
	{
        // Do nothing
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getIpAddress()
	 */
	public InetAddress getIpAddress()
	{
		return endpoint.getIpAddress();
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getPort()
	 */
	public int getPort()
	{
		return endpoint.getPort();
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getProtocol()
	 */
	public int getProtocol()
	{
		return endpoint.getProtocol();
	}
}
