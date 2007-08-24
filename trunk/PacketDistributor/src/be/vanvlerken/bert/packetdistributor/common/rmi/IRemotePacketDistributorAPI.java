/**
 * @author wItspirit
 * 28-dec-2004
 * IRemotePacketDistributorAPI.java
 */

package be.vanvlerken.bert.packetdistributor.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Remote Management API of the PacketDistributor
 */
public interface IRemotePacketDistributorAPI extends Remote
{
    public IRemoteTrafficRelay[] getRelays() throws RemoteException;
    
    public IRemoteTrafficRelay createRelay(String name) throws RemoteException;
    
    public void destroyRelay(IRemoteTrafficRelay relay) throws RemoteException;
        
}
