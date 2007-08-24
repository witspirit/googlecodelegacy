/**
 * @author wItspirit
 * 1-jan-2005
 * LocalPacketDistributorFacade.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.backend.impl;

import java.rmi.RemoteException;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.impl.rmi.RMIPacketDistributor;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade;


/**
 * This wraps a Local PacketDistributorAPI into a Facade
 */
public class LocalPacketDistributorFacade implements IPacketDistributorFacade
{
    private IPacketDistributorAPI local;
    private IRemotePacketDistributorAPI proxy;
    
    public LocalPacketDistributorFacade(IPacketDistributorAPI local)
    {
        this.local = local;
        try
        {
            proxy = new RMIPacketDistributor(local);
        }
        catch (RemoteException e)
        {
            // Should never happen, because it is ACTUALLY local !
            e.printStackTrace();
        }
    }
    
    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade#shutdown()
     */
    public void shutdown()
    {
        local.shutdown();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#getRelays()
     */
    public IRemoteTrafficRelay[] getRelays() throws RemoteException
    {
        return proxy.getRelays();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#createRelay(java.lang.String)
     */
    public IRemoteTrafficRelay createRelay(String name) throws RemoteException
    {
        return proxy.createRelay(name);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#destroyRelay(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay)
     */
    public void destroyRelay(IRemoteTrafficRelay relay) throws RemoteException
    {
        proxy.destroyRelay(relay);
    }

}
