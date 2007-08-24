/**
 * @author vlerkenb
 * 12-feb-2003
 * MixedModeServer.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * This will create a MixedModeLoggingSocket on the address found in the
 * Configuration and will decode the message and send it to the LogDb in an
 * appropriate way
 */
public class MixedModeServer implements LogServer
{
    private static Logger          logger = Logger
                                                  .getLogger(MixedModeServer.class);

    private ILogDatabase           logDb;
    private MixedModeLoggingSocket logSocket;
    private Thread                 listenThread;
    private ILogEntryDecoder       messageCoder;
    private InetSocketAddress      listenAddress;

    private volatile boolean       listen;

    public MixedModeServer(InetSocketAddress listenAddress, ILogDatabase logDb,
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
            logSocket = new MixedModeLoggingSocket(listenAddress);
            logSocket.setReceiveBufferSize(65536);
            logSocket.setSoTimeout(1000); /* 1 second */

            listen = true;
            listenThread = new Thread(this);
            listenThread.start();
        }
        catch (SocketException e)
        {
            logger.error("Could not create MixedModeLoggingSocket. "
                    + e.getMessage());
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
        logger.info("MixedModeServer started... Listening for LogPackets...");
        MixedModePacket packet = new MixedModePacket();
        while (listen)
        {
            try
            {
                logSocket.receive(packet);

                parsePacket(packet);
                packet = new MixedModePacket();
            }
            catch (SocketTimeoutException t)
            {
                /* This is normal, in order to have a clean shutdown */
            }
            catch (IOException e)
            {
                logger
                        .warn("Problem occured while reading from MixedModeLoggingSocket.\n"
                                + e.getMessage());
            }

        }
        logger.info("MixedModeServer shutting down...Closing listen socket...");
        logSocket.close();
        logger.info("MixedModeServer stopped.");
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
    private void parsePacket(MixedModePacket packet)
    {
        String seqNr;
        String id;
        ILogEntry logEntry;

        /* Straightforward mapping */
        seqNr = Long.toString(packet.getSeqNr());

        /* Translate packet identifier to local identifier */
        switch (packet.getType())
        {
        case 0:
            id = "ERROR";
            break;
        case 1:
            id = "WARNING";
            break;
        case 2:
            id = "INFO";
            break;
        case 3:
            id = "VERBOSE";
            break;
        default:
            id = "UNKNOWN";
        }

        /* Other LogEntry fields require parsing of the message */
        logEntry = messageCoder.decodeLogEntry(seqNr + ":" + id + ":"
                + packet.getMessage());

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