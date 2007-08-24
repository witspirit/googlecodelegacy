/**
 * @author wItspirit
 * 4-apr-2003
 * TrafficListener.java
 */
package be.vanvlerken.bert.packetdistributor.common;

import java.util.EventListener;

/**
 * Interface to receive traffic on from a ITrafficSource
 */
public interface TrafficListener extends EventListener
{
    public void trafficReceived(TrafficEvent trafficEvent);
}
