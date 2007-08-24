/**
 * @author wItspirit
 * 5-apr-2003
 * TrafficRelayImp.java
 */
package be.vanvlerken.bert.packetdistributor.common.impl.direct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.DeliveryException;
import be.vanvlerken.bert.packetdistributor.common.EndpointNotFoundException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;
import be.vanvlerken.bert.packetdistributor.common.TrafficEvent;
import be.vanvlerken.bert.packetdistributor.common.TrafficListener;

/**
 * Basic implementation of the ITrafficRelay interface
 */
public class TrafficRelayImp implements ITrafficRelay, TrafficListener
{
    private static final Log log = LogFactory.getLog(TrafficRelayImp.class);
    private int                       relayId;
    private String                    name;
    private List<ITrafficSource>      trafficSources;
    private List<ITrafficDestination> trafficDestinations;

    public TrafficRelayImp(int relayId, String name)
    {
        this.relayId = relayId;
        this.name = name;
        trafficSources = new ArrayList<ITrafficSource>();
        trafficDestinations = new ArrayList<ITrafficDestination>();
    }

    public TrafficRelayImp(int relayId)
    {
        this(relayId, "Unset");
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#getId()
     */
    public int getId()
    {
        return relayId;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#addTrafficSource(ITrafficSource)
     */
    public void addTrafficSource(ITrafficSource trafficSource)
            throws MaximumExceededException
    {
        trafficSources.add(trafficSource);
        trafficSource.addTrafficListener(this);
        trafficSource.startUp();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#removeTrafficSource(ITrafficSource)
     */
    public void removeTrafficSource(ITrafficSource trafficSource)
            throws EndpointNotFoundException
    {

        if (!trafficSources.remove(trafficSource)) { throw new EndpointNotFoundException(
                "Could not find trafficSource"); }
        trafficSource.cleanUp();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#addTrafficDestination(ITrafficDestination)
     */
    public void addTrafficDestination(ITrafficDestination trafficDestination)
            throws MaximumExceededException
    {
        trafficDestinations.add(trafficDestination);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#removeTrafficDestination(ITrafficDestination)
     */
    public void removeTrafficDestination(ITrafficDestination trafficDestination)
            throws EndpointNotFoundException
    {
        if (!trafficDestinations.remove(trafficDestination)) { throw new EndpointNotFoundException(
                "Could not find trafficDestination"); }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.TrafficListener#trafficReceived(TrafficEvent)
     */
    public void trafficReceived(TrafficEvent trafficEvent)
    {
        List<ITrafficDestination> safeDestinations = new ArrayList<ITrafficDestination>(
                trafficDestinations);
        for (ITrafficDestination trafficDestination : safeDestinations)
        {
            try
            {
                trafficDestination.send(trafficEvent.getDataChunk());
            }
            catch (DeliveryException e)
            {
                log.warn(
                        "Undeliverable datachunk silently dropped");
            }
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#getDestinations()
     */
    public ITrafficDestination[] getDestinations()
    {
        ITrafficDestination[] tempArray = new ITrafficDestination[0];
        return trafficDestinations.toArray(tempArray);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.ITrafficRelay#getSources()
     */
    public ITrafficSource[] getSources()
    {
        ITrafficSource[] tempArray = new ITrafficSource[0];
        return trafficSources.toArray(tempArray);
    }

    public void cleanUp()
    {
        for (ITrafficSource trafficSource : trafficSources)
        {
            trafficSource.cleanUp();
        }

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
