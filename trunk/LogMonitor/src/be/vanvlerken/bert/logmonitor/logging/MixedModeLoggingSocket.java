/**
 * @author vlerkenb
 * 10-feb-2003
 * MixedModeLoggingSocket.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * A logging socket it will be able to send and receive LogPackets
 * It's service is implemented on top of a UDP (Datagram) socket
 */
public class MixedModeLoggingSocket extends DatagramSocket
{
	/**
	 * Constructor for MixedModeLoggingSocket.
	 * @throws SocketException
	 */
	public MixedModeLoggingSocket() throws SocketException
	{
		super();
	}

	/**
	 * Constructor for MixedModeLoggingSocket.
	 * @param bindaddr
	 * @throws SocketException
	 */
	public MixedModeLoggingSocket(SocketAddress bindaddr) throws SocketException
	{
		super(bindaddr);
	}

	/**
	 * Constructor for MixedModeLoggingSocket.
	 * @param port
	 * @throws SocketException
	 */
	public MixedModeLoggingSocket(int port) throws SocketException
	{
		super(port);
	}

	/**
	 * Constructor for MixedModeLoggingSocket.
	 * @param port
	 * @param laddr
	 * @throws SocketException
	 */
	public MixedModeLoggingSocket(int port, InetAddress laddr) throws SocketException
	{
		super(port, laddr);
	}

    public void send(MixedModePacket packet) throws IOException
    {
        super.send(packet.getUdpPacket());
    }
    
    public void receive(MixedModePacket packet) throws IOException, SocketTimeoutException
    {
        super.receive(packet.getUdpPacket());
    }
}