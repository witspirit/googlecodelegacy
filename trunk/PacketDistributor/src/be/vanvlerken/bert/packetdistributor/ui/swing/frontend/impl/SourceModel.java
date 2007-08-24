/**
 * @author wItspirit
 * 15-mei-2003
 * SourceModel.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import java.rmi.RemoteException;

import be.vanvlerken.bert.packetdistributor.common.EndpointNotFoundException;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;

/**
 * Models the Sources of a relay
 */
public class SourceModel extends TrafficEntityModel
{
    public SourceModel()
    {
        super();
    }
    
    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getTitle()
     */
    public String getTitle()
    {
        return "Sources";
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getElementName()
     */
    public String getElementName()
    {
        return "Source";
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.TrafficEntityModel#getEntities()
     */
    protected ITrafficEndpoint[] getEntities(IRemoteTrafficRelay relay)
    {
        try
        {
            return relay.getSources();
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
            relay.addTrafficSource(te);
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
            relay.removeTrafficSource(te);
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
