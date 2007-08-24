/**
 * @author wItspirit
 * 28-dec-2004
 * RMITrafficRelay.java
 */

package be.vanvlerken.bert.packetdistributor.common.impl.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import be.vanvlerken.bert.packetdistributor.common.EndpointNotFoundException;
import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;
import be.vanvlerken.bert.packetdistributor.common.TrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;


/**
 * RMI based server
 */
public class RMITrafficRelay extends UnicastRemoteObject implements IRemoteTrafficRelay
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257564018590955314L;
    private IPacketDistributorAPI pd;
    private ITrafficRelay localRelay;
    private Map<ITrafficEndpoint, ITrafficSource> localTrafficSources;
    private Map<ITrafficEndpoint, ITrafficDestination> localTrafficDestinations;
    
    /**
     * @param localRelay
     * @throws RemoteException 
     */
    public RMITrafficRelay(IPacketDistributorAPI pd, ITrafficRelay localRelay) throws RemoteException
    {
        super();
        this.pd = pd;
        this.localRelay = localRelay;
        localTrafficSources = new HashMap<ITrafficEndpoint, ITrafficSource>();
        localTrafficDestinations = new HashMap<ITrafficEndpoint, ITrafficDestination>();
        initRemoteMappings();        
    }

    /**
     * 
     */
    private void initRemoteMappings()
    {
        for ( ITrafficSource localSource : localRelay.getSources() )
        {
            TrafficEndpoint endpoint = new TrafficEndpoint(localSource);
            localTrafficSources.put(endpoint, localSource);
        }
        for ( ITrafficDestination localDestination : localRelay.getDestinations() )
        {
            TrafficEndpoint endpoint = new TrafficEndpoint(localDestination);
            localTrafficDestinations.put(endpoint, localDestination);
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#getId()
     */
    public int getId() throws RemoteException
    {
        return localRelay.getId();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#getName()
     */
    public String getName() throws RemoteException
    {
        return localRelay.getName();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#setName(java.lang.String)
     */
    public void setName(String name) throws RemoteException
    {
        localRelay.setName(name);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#addTrafficSource(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficSource)
     */
    public void addTrafficSource(ITrafficEndpoint trafficSource)
            throws MaximumExceededException, RemoteException
    {
        TrafficEndpoint endpoint = new TrafficEndpoint(trafficSource);
        ITrafficSource localTrafficSource = pd.createTrafficSource(endpoint);
        localTrafficSources.put(endpoint, localTrafficSource);
        localRelay.addTrafficSource(localTrafficSource);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#removeTrafficSource(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficSource)
     */
    public void removeTrafficSource(ITrafficEndpoint trafficSource)
            throws EndpointNotFoundException, RemoteException
    {
        ITrafficSource localSource = localTrafficSources.remove(trafficSource);
        localRelay.removeTrafficSource(localSource);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#addTrafficDestination(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficDestination)
     */
    public void addTrafficDestination(
            ITrafficEndpoint trafficDestination)
            throws MaximumExceededException, RemoteException
    {
        TrafficEndpoint endpoint = new TrafficEndpoint(trafficDestination);
        ITrafficDestination localTrafficDestination = pd.createTrafficDestination(endpoint);
        localTrafficDestinations.put(endpoint, localTrafficDestination);
        localRelay.addTrafficDestination(localTrafficDestination);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#removeTrafficDestination(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficDestination)
     */
    public void removeTrafficDestination(
            ITrafficEndpoint trafficDestination)
            throws EndpointNotFoundException, RemoteException
    {
        ITrafficDestination localDestination = localTrafficDestinations.remove(trafficDestination);
        localRelay.removeTrafficDestination(localDestination);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#getSources()
     */
    public ITrafficEndpoint[] getSources() throws RemoteException
    {
        return localTrafficSources.keySet().toArray(new ITrafficEndpoint[0]);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay#getDestinations()
     */
    public ITrafficEndpoint[] getDestinations() throws RemoteException
    {
        return localTrafficDestinations.keySet().toArray(new ITrafficEndpoint[0]);
    }

}
