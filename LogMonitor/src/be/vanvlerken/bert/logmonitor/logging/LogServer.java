/*
 * @author vlerkenb
 * 5-mei-2004
 * LogServer.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.net.InetSocketAddress;

/**
 * description
 */
public interface LogServer extends Runnable
{
    public abstract void activate();

    public abstract void deactivate();

    /**
     * @see java.lang.Runnable#run()
     */
    public abstract void run();

    /**
     * @return
     */
    public abstract InetSocketAddress getListenAddress();
}