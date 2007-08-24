/**
 * @author wItspirit 2-feb-2005 TcpTrafficSource.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.direct;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.DataChunk;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.TrafficEvent;
import be.vanvlerken.bert.packetdistributor.common.TrafficListener;

/**
 * Listens for TCP traffic on a certain socket and delivers it to a listener
 */
public class TcpTrafficSource extends Thread implements ITrafficSource
{
    private static final Log      log = LogFactory.getLog(TcpTrafficSource.class);
    private ServerSocket          tcpSocket;
    private Socket                connectedSocket;
    private List<TrafficListener> listeners;
    private boolean               active;

    public TcpTrafficSource(InetSocketAddress socketAddress) throws IOException
    {
        connectedSocket = null;
        tcpSocket = new ServerSocket();
        tcpSocket.bind(socketAddress);
        tcpSocket.setReceiveBufferSize(65535);
        listeners = new ArrayList<TrafficListener>();
        active = true;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficSource#addTrafficListener(TrafficListener)
     */
    public void addTrafficListener(TrafficListener trafficListener)
    {
        listeners.add(trafficListener);
    }

    public void fireTrafficEvent(TrafficEvent trafficEvent)
    {
        List<TrafficListener> safeListeners = new ArrayList<TrafficListener>(listeners);
        for (TrafficListener trafficListener : safeListeners)
        {
            trafficListener.trafficReceived(trafficEvent);
        }
    }

    public void cleanUp()
    {
        active = false;
        try
        {
            if (connectedSocket != null)
            {
                connectedSocket.close();
            }
            tcpSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        while (active)
        {
            try
            {
                connectedSocket = tcpSocket.accept();
                // We have a connection...
                try
                {
                    InputStream receiveStream = connectedSocket.getInputStream();

                    while (active)
                    {
                        DataChunk dataChunk = new DataChunk(1500);

                        int bytesRead = receiveStream.read(dataChunk.getBytes(), 0, dataChunk.getSize());

                        dataChunk.setDataLength(bytesRead);
                        TrafficEvent trafficEvent = new TrafficEvent(this, dataChunk);
                        fireTrafficEvent(trafficEvent);
                    }
                }
                catch (IOException e)
                {
                    log.info("Connected Socket was closed.");
                }
            }
            catch (IOException ie)
            {
                log.info("Listener Socket was closed.");
            }
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getIpAddress()
     */
    public InetAddress getIpAddress()
    {
        return tcpSocket.getInetAddress();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getPort()
     */
    public int getPort()
    {
        return tcpSocket.getLocalPort();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getProtocol()
     */
    public int getProtocol()
    {
        return ITrafficEndpoint.TCP;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#startUp()
     */
    public void startUp()
    {
        start();
    }
}
