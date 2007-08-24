/*
 * @author witspirit
 * 16-jun-2003
 * PdDummyInterface.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.dummy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;

/**
 * This is a dummy implementation of the IPacketDistributorAPI. It creates
 * objects that can hold dummy values to mimic the display as if it were the
 * real objects. However they do not perform any of the associated behavior.
 */
public class PdDummyInterface implements IPacketDistributorAPI
{
    private Map<Integer, ITrafficRelay> relays;

    public PdDummyInterface()
    {
        relays = new HashMap<Integer, ITrafficRelay>();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#getRelays()
     */
    public Collection<ITrafficRelay> getRelays()
    {
        return relays.values();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#createRelay()
     */
    public ITrafficRelay createRelay(String name)
    {
        int id = (int) (Math.random() * 65535);
        while (relays.containsKey(id))
        {
            id = (int) (Math.random() * 65535);
        }

        ITrafficRelay relay = new DummyTrafficRelay(id, name);
        relays.put(id, relay);

        return relay;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#createTrafficSource(java.net.InetAddress,
     *      int, int)
     */
    public ITrafficSource createTrafficSource(ITrafficEndpoint endpoint)
    {
        return new DummyTrafficSource(endpoint);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#createTrafficDestination(java.net.InetAddress,
     *      int, int)
     */
    public ITrafficDestination createTrafficDestination(
            ITrafficEndpoint endpoint)
    {
        return new DummyTrafficDestination(endpoint);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.IPacketDistributorAPI#destroyRelay(be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay)
     */
    public void destroyRelay(ITrafficRelay relay)
    {
        relay.cleanUp();
        relays.remove(relay.getId());
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI#shutdown()
     */
    public void shutdown()
    {
        for (ITrafficRelay relay : getRelays())
        {
            relay.cleanUp();
        }
        relays.clear();
    }
}
