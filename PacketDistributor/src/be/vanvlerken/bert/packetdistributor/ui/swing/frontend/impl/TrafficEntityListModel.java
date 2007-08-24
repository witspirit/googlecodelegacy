/**
 * @author wItspirit
 * 28-apr-2003
 * TrafficEntityListModel.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import javax.swing.AbstractListModel;

import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;

/**
 * The ListModel for displaying the list of Sources
 */
public class TrafficEntityListModel extends AbstractListModel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4048792372886124343L;
    private ITrafficEndpoint[]    entities;
    
    public TrafficEntityListModel(ITrafficEndpoint[] entities)
    {
        this.entities = entities;
    }
    
    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index)
    {
        ITrafficEndpoint te = entities[index];
        String protocol;
        switch ( te.getProtocol() )
        {
            case ITrafficEndpoint.TCP : protocol = "TCP"; break;
            case ITrafficEndpoint.UDP : protocol = "UDP"; break;
            default: protocol = "UNKNOWN"; break;
        }
        return te.getIpAddress()+":"+protocol+":"+Integer.toString(te.getPort());
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize()
    {
        return entities.length;
    }

    public void fireUpdated(ITrafficEndpoint[] entities)
    {
        this.entities = entities;
        this.fireContentsChanged(this, 0, getSize());
    }
}
