/**
 * @author wItspirit
 * 1-jan-2005
 * IPacketDistributorFacade.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.backend;

import be.vanvlerken.bert.packetdistributor.common.rmi.IRemotePacketDistributorAPI;


/**
 * Adds a shutdown method to the remote handling in order to be able to hide
 * a local implementation behind this Facade
 */
public interface IPacketDistributorFacade extends IRemotePacketDistributorAPI
{
    public abstract void shutdown();
}
