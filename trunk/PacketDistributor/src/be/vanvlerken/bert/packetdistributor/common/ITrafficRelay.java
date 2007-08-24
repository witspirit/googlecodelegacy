/**
 * @author wItspirit
 * 4-apr-2003
 * ITrafficRelay.java
 */
package be.vanvlerken.bert.packetdistributor.common;


/**
 * The API definition of a ITrafficRelay implementation. A ITrafficRelay takes one or more
 * TrafficSources and TrafficDestinations and relays the DataChunks from the Sources to
 * the Destinations.
 */
public interface ITrafficRelay
{
    public int  getId();
    public void cleanUp();
    
    public String getName();
    public void setName(String name);
    
    public void addTrafficSource(ITrafficSource trafficSource) throws MaximumExceededException;
    public void removeTrafficSource(ITrafficSource trafficSource) throws EndpointNotFoundException;
    
    public void addTrafficDestination(ITrafficDestination trafficDestination) throws MaximumExceededException;
    public void removeTrafficDestination(ITrafficDestination trafficDestination) throws EndpointNotFoundException;
    
    public ITrafficSource[] getSources();
    public ITrafficDestination[] getDestinations();
}
