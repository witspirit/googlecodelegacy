/*
 * @author vlerkenb
 * 5-mei-2004
 * StringServer.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * This server just listens on a UDP socket and retrieves packets containing
 * only string data
 */
public class StringServer implements LogServer
{
    private static Logger     logger       = Logger
                                                   .getLogger(StringServer.class);
    private static final int  packetLength = 1500;

    private ILogDatabase      logDb;
    private DatagramSocket    logSocket;
    private Thread            listenThread;
    private ILogEntryDecoder  messageCoder;
    private InetSocketAddress listenAddress;

    private volatile boolean  listen;

    public StringServer(InetSocketAddress listenAddress, ILogDatabase logDb,
            ILogEntryDecoder messageDecoder)
    {
        this.logDb = logDb;
        this.listenAddress = listenAddress;
        this.messageCoder = messageDecoder;
    }

    public void activate()
    {
        try
        {
            logSocket = new DatagramSocket(listenAddress);
            logSocket.setReceiveBufferSize(65536);
            logSocket.setSoTimeout(1000); /* 1 second */

            listen = true;
            listenThread = new Thread(this);
            listenThread.start();
        }
        catch (SocketException e)
        {
            logger.error("Could not create DatagramSocket. " + e.getMessage());
        }
    }

    public void deactivate()
    {
        listen = false;
        synchronized (this)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        logger.info("StringServer started... Listening for packets...");
        DatagramPacket packet = new DatagramPacket(new byte[packetLength],
                packetLength);
        while (listen)
        {
            try
            {
                logSocket.receive(packet);

                parsePacket(packet);

                packet = new DatagramPacket(new byte[packetLength],
                        packetLength);
            }
            catch (SocketTimeoutException t)
            {
                /* This is normal, in order to have a clean shutdown */
            }
            catch (IOException e)
            {
                logger
                        .warn("Problem occured while reading from DatagramSocket.\n"
                                + e.getMessage());
            }

        }
        logger.info("StringServer shutting down...Closing listen socket...");
        logSocket.close();
        logger.info("StringServer stopped.");
        synchronized (this)
        {
            notifyAll();
        }
    }

    /**
     * Method parsePacket.
     * 
     * @param packet
     */
    private void parsePacket(DatagramPacket packet)
    {
        String logString;
        ILogEntry logEntry;

        // Extract a clean string from the datagram packet
        byte[] byteArray = packet.getData();
        int i = 0;
        while (byteArray[i] != 0)
            i++;
        logString = new String(packet.getData(), 0, i);
        
        logger.debug("Received Log String: "+logString);

        /* Just pass on the received String */
        logEntry = messageCoder.decodeLogEntry(logString);

        /* All data collected... Create the entry */
        logDb.add(logEntry);
    }

    /**
     * @return
     */
    public InetSocketAddress getListenAddress()
    {
        return listenAddress;
    }

}