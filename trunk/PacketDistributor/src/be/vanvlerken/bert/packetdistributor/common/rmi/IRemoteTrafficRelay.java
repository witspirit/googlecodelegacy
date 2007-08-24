/**
 * @author wItspirit
 * 28-dec-2004
 * IRemoteTrafficRelay.java
 */

package be.vanvlerken.bert.packetdistributor.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import be.vanvlerken.bert.packetdistributor.common.EndpointNotFoundException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;


/**
 * The remote management interface for a TrafficRelay
 */
public interface IRemoteTrafficRelay extends Remote
{
    public int  getId() throws RemoteException;    
    
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
    
    public void addTrafficSource(ITrafficEndpoint trafficSource) throws MaximumExceededException, RemoteException;
    public void removeTrafficSource(ITrafficEndpoint trafficSource) throws EndpointNotFoundException, RemoteException;
    
    public void addTrafficDestination(ITrafficEndpoint trafficDestination) throws MaximumExceededException, RemoteException;
    public void removeTrafficDestination(ITrafficEndpoint trafficDestination) throws EndpointNotFoundException, RemoteException;
    
    public ITrafficEndpoint[] getSources() throws RemoteException;
    public ITrafficEndpoint[] getDestinations() throws RemoteException;
}
