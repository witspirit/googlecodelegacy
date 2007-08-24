/**
 * @author wItspirit
 * 12-mei-2003
 * DummyTrafficRelay.java
 */

package be.vanvlerken.bert.packetdistributor.common.impl.dummy;

import java.util.ArrayList;
import java.util.List;

import be.vanvlerken.bert.packetdistributor.common.EndpointNotFoundException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;

/**
 * Provides an Application Facade for the PacketDistributorServer classes
 */
public class DummyTrafficRelay implements ITrafficRelay
{
    private int                       id;
    private String                    name;
    private List<ITrafficSource>      sources;
    private List<ITrafficDestination> destinations;

    public DummyTrafficRelay(int relayId)
    {
        this(relayId, "Unset");
    }

    /**
     * @param relayId
     * @param name
     */
    public DummyTrafficRelay(int relayId, String name)
    {
        this.id = relayId;
        this.name = name;

        sources = new ArrayList<ITrafficSource>();
        destinations = new ArrayList<ITrafficDestination>();
    }

    public ITrafficSource[] getSources()
    {
        ITrafficSource[] tsArray = new ITrafficSource[0];
        return sources.toArray(tsArray);
    }

    public ITrafficDestination[] getDestinations()
    {
        ITrafficDestination[] tdArray = new ITrafficDestination[0];
        return destinations.toArray(tdArray);
    }

    /**
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#cleanUp()
     */
    public void cleanUp()
    {
        // Nothing special to be done
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#addTrafficSource(be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource)
     */
    public void addTrafficSource(ITrafficSource trafficSource)
            throws MaximumExceededException
    {
        sources.add(trafficSource);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#removeTrafficSource(be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficSource)
     */
    public void removeTrafficSource(ITrafficSource trafficSource)
            throws EndpointNotFoundException
    {
        sources.remove(trafficSource);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#addTrafficDestination(be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination)
     */
    public void addTrafficDestination(ITrafficDestination trafficDestination)
            throws MaximumExceededException
    {
        destinations.add(trafficDestination);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#removeTrafficDestination(be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficDestination)
     */
    public void removeTrafficDestination(ITrafficDestination trafficDestination)
            throws EndpointNotFoundException
    {
        destinations.remove(trafficDestination);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#getName()
     */
    public String getName()
    {
        return name;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.generics.ITrafficRelay#setName(java.lang.String)
     */
    public void setName(String name)
    {
        this.name = name;
    }
}
