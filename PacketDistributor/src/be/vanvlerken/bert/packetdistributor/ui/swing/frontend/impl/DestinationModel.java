/**
 * @author wItspirit
 * 15-mei-2003
 * DestinationModel.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import java.rmi.RemoteException;

import be.vanvlerken.bert.packetdistributor.common.EndpointNotFoundException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;

/**
 * Models the Destinations of a relay
 */
public class DestinationModel extends TrafficEntityModel
{
    public DestinationModel()
    {
        super();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getTitle()
     */
    public String getTitle()
    {
        return "Destinations";
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getElementName()
     */
    public String getElementName()
    {
        return "Destination";
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.TrafficEntityModel#getEntities(be.vanvlerken.bert.packetdistributor.ui.console.gui.DummyTrafficRelay)
     */
    protected ITrafficEndpoint[] getEntities(IRemoteTrafficRelay relay)
    {
        try
        {
            return relay.getDestinations();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return new ITrafficEndpoint[0];
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.TrafficEntityModel#addEntity(be.vanvlerken.bert.packetdistributor.ui.console.gui.DummyTrafficRelay, be.vanvlerken.bert.packetdistributor.ui.console.gui.TrafficEntityFacade)
     */
    protected void addEntity(IRemoteTrafficRelay relay, ITrafficEndpoint te)
    {
        try
        {
            relay.addTrafficDestination(te);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (MaximumExceededException e)
        {
            e.printStackTrace();
        }
    }

	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.TrafficEntityModel#removeEntity(be.vanvlerken.bert.packetdistributor.ui.console.gui.DummyTrafficRelay, int)
	 */
	protected void removeEntity(IRemoteTrafficRelay relay, ITrafficEndpoint te)
	{
        try
        {
            relay.removeTrafficDestination(te);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (EndpointNotFoundException e)
        {
            e.printStackTrace();
        }
	}

}
