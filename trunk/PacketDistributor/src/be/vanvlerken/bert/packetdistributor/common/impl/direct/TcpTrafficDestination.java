/**
 * @author wItspirit 2-feb-2005 TcpTrafficDestination.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.direct;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.DataChunk;
import be.vanvlerken.bert.packetdistributor.common.DeliveryException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;

/**
 * Sends UDP traffic to an interested host
 */
public class TcpTrafficDestination implements ITrafficDestination
{
    private static final Log  log = LogFactory.getLog(TcpTrafficDestination.class);
    private Socket            tcpSocket;
    private InetSocketAddress destinationAddress;
    private OutputStream      sendStream;

    public TcpTrafficDestination(InetSocketAddress destinationAddress) throws SocketException
    {
        this.destinationAddress = destinationAddress;
        sendStream = null;
        tcpSocket = new Socket();
        tcpSocket.setSendBufferSize(65535);
        connect();
    }

    /**
     * 
     */
    private boolean connect()
    {
        if (!tcpSocket.isConnected())
        {
            try
            {
                tcpSocket.connect(destinationAddress);
            }
            catch (IOException e)
            {
                log.warn("Failed to connect to " + destinationAddress + ". Ignoring for now, will retry later");
                return false;
            }
        }
        if (sendStream == null)
        {
            try
            {
                sendStream = tcpSocket.getOutputStream();
            }
            catch (IOException e)
            {
                log.warn("Cannot obtain OutputStream.");
                return false;
            }
        }
        return true;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficDestination#send(DataChunk)
     */
    public void send(DataChunk data) throws DeliveryException
    {
        try
        {
            if (connect())
            {
                sendStream.write(data.getBytes(), 0, data.getDataLength());
                sendStream.flush();
            }
        }
        catch (IOException e)
        {
            try
            {
                sendStream.close();
            }
            catch (IOException e2)
            {
            }
            finally
            {
                sendStream = null;
            }
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
        return ITrafficEndpoint.TCP;
    }
}
