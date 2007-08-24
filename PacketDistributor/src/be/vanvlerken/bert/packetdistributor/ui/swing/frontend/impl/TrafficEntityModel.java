/**
 * @author wItspirit
 * 28-apr-2003
 * TrafficEntityModel.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.TrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.AddRemoveModel;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.SelectionEventObject;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.SelectionListener;

/**
 * The model for displaying TrafficEntity information
 */
abstract public class TrafficEntityModel implements AddRemoveModel, SelectionListener
{
    private TrafficEntityListModel             model;
    private TrafficEndpointJComponent   inputComponent;
    
    private ITrafficEndpoint[]    entities;
    private IRemoteTrafficRelay               relay;
    
    public TrafficEntityModel()
    {
        relay = null;
        entities = new ITrafficEndpoint[0];
        
        model = new TrafficEntityListModel(entities);
        
        inputComponent = new TrafficEndpointJComponent();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getTitle()
     */
    abstract public String getTitle();
    
    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getElementName()
     */
    abstract public String getElementName();
    
    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getListModel()
     */
    public ListModel getListModel()
    {
        return model;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#remove(int)
     */
    public void remove(int selectedItem)
    {
        try
        {
            removeEntity(relay, entities[selectedItem]);
        }
        catch (IndexOutOfBoundsException ioobe)
        {
            JOptionPane.showMessageDialog(inputComponent, "Invalid selection !", "Invalid Selection !", JOptionPane.ERROR_MESSAGE);
            return;
        }
        entities = getEntities(relay);
        inputComponent.clearFields();
        model.fireUpdated(entities);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#add()
     */
    public void add()
    {
        InetAddress ipAddress;
        try
        {
            ipAddress = inputComponent.getIpAddress();
        }
        catch (UnknownHostException uhe)
        {
            JOptionPane.showMessageDialog(inputComponent, uhe.getMessage(), "Invalid IP address", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int protocol = inputComponent.getProtocol();
        int port;
        try
        {
            port = inputComponent.getPort();
        }
        catch (NumberFormatException nfe)
        {
            JOptionPane.showMessageDialog(inputComponent, nfe.getMessage(), "Malformed port value", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ITrafficEndpoint te = new TrafficEndpoint(ipAddress, port, protocol);
        if ( relay == null )
        {
            System.out.println("Uhh ?");
        }
        addEntity(relay,te);
        entities = getEntities(relay);
        inputComponent.clearFields();
        model.fireUpdated(entities);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getInputComponent()
     */
    public JComponent getInputComponent()
    {
        return inputComponent;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#selected(int)
     */
    public void selected(int selectedItem)
    {
        if ( selectedItem == -1 )
        {
            inputComponent.clearFields();
            return;
        }
        
        ITrafficEndpoint te = entities[selectedItem];
        inputComponent.setIpAddress(te.getIpAddress());
        inputComponent.setProtocol(te.getProtocol());
        inputComponent.setPort(te.getPort());
    }



    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.SelectionListener#selected(be.vanvlerken.bert.packetdistributor.ui.console.gui.SelectionEventObject)
     */
    public void selected(SelectionEventObject seo)
    {
        // System.out.println(getTitle()+" received relay updated !");
        relay = seo.getRelay();
        if ( relay == null)
        {
            entities = new ITrafficEndpoint[0];
            
        }
        else
        {
            entities = getEntities(relay);
        }
        model.fireUpdated(entities);
    }
    
    abstract protected ITrafficEndpoint[] getEntities(IRemoteTrafficRelay relay);
    abstract protected void addEntity(IRemoteTrafficRelay relay, ITrafficEndpoint te);
    abstract protected void removeEntity(IRemoteTrafficRelay relay, ITrafficEndpoint te);
    
	/**
	 * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#isActive()
	 */
	public boolean isActive()
	{
		return relay!=null;
	}

}
