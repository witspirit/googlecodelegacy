/**
 * @author wItspirit
 * 5-apr-2003
 * UdpTrafficDestination.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.direct;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import be.vanvlerken.bert.packetdistributor.common.DataChunk;
import be.vanvlerken.bert.packetdistributor.common.DeliveryException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;

/**
 * Sends UDP traffic to an interested host
 */
public class UdpTrafficDestination implements ITrafficDestination
{
    private DatagramSocket      udpSocket;
    private InetSocketAddress   destinationAddress;
    
    public UdpTrafficDestination(InetSocketAddress destinationAddress) throws SocketException
    {
        this.destinationAddress = destinationAddress;
        udpSocket = new DatagramSocket();
        udpSocket.setSendBufferSize(65535); 
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficDestination#send(DataChunk)
     */
    public void send(DataChunk data) throws DeliveryException
    {
        try
        {
            DatagramPacket dgPacket = new DatagramPacket(data.getBytes(), data.getDataLength(), destinationAddress);
            udpSocket.send(dgPacket);
        }
        catch (IOException e)
        {
            throw new DeliveryException(e.getMessage());
        }
    }

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#getIpAddress()
	 */
	public InetAddress getIpAddress()
	{
		return destinationAddress.getAddress();
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#getPort()
	 */
	public int getPort()
	{
		return destinationAddress.getPort();
	}

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination#getProtocol()
	 */
	public int getProtocol()
	{
		return ITrafficEndpoint.UDP;
	}

}
