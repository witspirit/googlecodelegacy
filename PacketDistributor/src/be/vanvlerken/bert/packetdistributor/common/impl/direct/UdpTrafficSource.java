/**
 * @author wItspirit
 * 5-apr-2003
 * UdpTrafficSource.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.direct;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
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
 * Listens for UDP traffic on a certain socket and delivers it to a listener
 */
public class UdpTrafficSource extends Thread implements ITrafficSource
{
    private static final Log      log = LogFactory
                                              .getLog(UdpTrafficSource.class);
    private DatagramSocket        udpSocket;
    private List<TrafficListener> listeners;
    private boolean               active;

    public UdpTrafficSource(InetSocketAddress socketAddress)
            throws SocketException
    {
        udpSocket = new DatagramSocket(socketAddress);
        udpSocket.setReceiveBufferSize(65535);
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
        List<TrafficListener> safeListeners = new ArrayList<TrafficListener>(
                listeners);
        for (TrafficListener trafficListener : safeListeners)
        {
            trafficListener.trafficReceived(trafficEvent);
        }
    }

    public void cleanUp()
    {
        active = false;
        udpSocket.close();
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
                DataChunk dataChunk = new DataChunk(1500);
                DatagramPacket dgPacket = new DatagramPacket(dataChunk
                        .getBytes(), dataChunk.getSize());

                udpSocket.receive(dgPacket);

                dataChunk.setDataLength(dgPacket.getLength());
                TrafficEvent trafficEvent = new TrafficEvent(this, dataChunk);
                fireTrafficEvent(trafficEvent);
            }
            catch (IOException ie)
            {
                log.info("Socket was closed.");
            }
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getIpAddress()
     */
    public InetAddress getIpAddress()
    {
        return udpSocket.getLocalAddress();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getPort()
     */
    public int getPort()
    {
        return udpSocket.getLocalPort();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#getProtocol()
     */
    public int getProtocol()
    {
        return ITrafficEndpoint.UDP;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource#startUp()
     */
    public void startUp()
    {
        start();
    }
}
