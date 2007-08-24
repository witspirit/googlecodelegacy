/**
 * @author wItspirit
 * 1-jan-2005
 * RemotePacketDistributorFacade.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.backend.impl;

import java.rmi.RemoteException;

import be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade;


/**
 * This Facade implementation wraps a remote interface
 */
public class RemotePacketDistributorFacade implements IPacketDistributorFacade
{
    private IRemotePacketDistributorAPI remote;
    
    public RemotePacketDistributorFacade(IRemotePacketDistributorAPI remote)
    {
        if ( remote == null )
        {
            throw new NullPointerException("Remote PacketDistributor not properly initialized");
        }
        this.remote = remote;
    }
    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade#shutdown()
     */
    public void shutdown()
    {
        // Do nothing
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#getRelays()
     */
    public IRemoteTrafficRelay[] getRelays() throws RemoteException
    {
        return remote.getRelays();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#createRelay(java.lang.String)
     */
    public IRemoteTrafficRelay createRelay(String name) throws RemoteException
    {
        return remote.createRelay(name);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI#destroyRelay(be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay)
     */
    public void destroyRelay(IRemoteTrafficRelay relay) throws RemoteException
    {
        remote.destroyRelay(relay);
    }

}
