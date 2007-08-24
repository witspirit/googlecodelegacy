/**
 * @author wItspirit
 * 4-apr-2003
 * ITrafficSource.java
 */
package be.vanvlerken.bert.packetdistributor.common;

/**
 * The API definition for receiving traffic from a certain source, using a certain protocol
 */
public interface ITrafficSource extends ITrafficEndpoint
{
    public abstract void addTrafficListener(TrafficListener trafficListener);
    public abstract void startUp();
    public abstract void cleanUp();    
}
