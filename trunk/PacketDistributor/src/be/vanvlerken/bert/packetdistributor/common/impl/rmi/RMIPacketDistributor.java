/**
 * @author wItspirit
 * 28-dec-2004
 * RMIPacketDistributor.java
 */

package be.vanvlerken.bert.packetdistributor.common.impl.rmi;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;


/**
 * RMI based server
 */
public class RMIPacketDistributor implements IRemotePacketDistributorAPI
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258689901401487153L;
    
    private IPacketDistributorAPI pd;
    private Map<IRemoteTrafficRelay, ITrafficRelay> remoteRelays;
    
    public RMIPacketDistributor(IPacketDistributorAPI pd) throws RemoteException
    {
        super();
        this.pd = pd;
        remoteRelays = new HashMap<IRemoteTrafficRelay, ITrafficRelay>();
        initRemoteRelays();
    }

    /**
     * @throws RemoteException 
     * 
     */
    private void initRemoteRelays() throws RemoteException
    {
        Collection<ITrafficRelay> localRelays = pd.getRelays();
        for ( ITrafficRelay localRelay : localRelays )
        {
            RMITrafficRelay remoteRelay = new RMITrafficRelay(pd, localRelay);
            remoteRelays.put(remoteRelay,localRelay);
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#getRelays()
     */
    public IRemoteTrafficRelay[] getRelays() throws RemoteException
    {
        return remoteRelays.keySet().toArray(new IRemoteTrafficRelay[0]);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#createRelay(java.lang.String)
     */
    public IRemoteTrafficRelay createRelay(String name) throws RemoteException
    {
        ITrafficRelay localRelay = pd.createRelay(name);
        RMITrafficRelay remoteRelay = new RMITrafficRelay(pd, localRelay);
        remoteRelays.put(remoteRelay, localRelay);
        return remoteRelay;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#destroyRelay(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay)
     */
    public void destroyRelay(IRemoteTrafficRelay relay) throws RemoteException
    {
        ITrafficRelay localRelay = remoteRelays.remove(relay);
        pd.destroyRelay(localRelay);
    }

}
