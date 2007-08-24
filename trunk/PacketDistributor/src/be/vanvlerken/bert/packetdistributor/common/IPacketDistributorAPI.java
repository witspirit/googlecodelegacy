/*
 * @author witspirit
 * 13-jun-2003
 * IPacketDistributorAPI.java
 */
package be.vanvlerken.bert.packetdistributor.common;

import java.util.Collection;

/**
 * Provides the basic API towards the PacketDistributorServer functionalities
 */
public interface IPacketDistributorAPI
{
    public Collection<ITrafficRelay> getRelays();
    
    public ITrafficRelay createRelay(String name);
    public ITrafficSource createTrafficSource(ITrafficEndpoint endpoint);
    public ITrafficDestination createTrafficDestination(ITrafficEndpoint endpoint);
    
    public void destroyRelay(ITrafficRelay relay);
    
    public void shutdown();
}
