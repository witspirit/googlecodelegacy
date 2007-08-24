/**
 * @author wItspirit
 * 27-apr-2003
 * RelayListModel.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import java.rmi.RemoteException;

import javax.swing.AbstractListModel;

import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;

/**
 * The ListModel for displaying the list of Relays
 */
public class RelayListModel extends AbstractListModel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3618135667142767413L;
    private IRemoteTrafficRelay[]    relays;
    
    public RelayListModel(IRemoteTrafficRelay[] relays)
    {
        this.relays = relays;    
    }
    
    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize()
    {
        return relays.length;
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index)
    {
        IRemoteTrafficRelay relay = getRelay(index);
        try
        {
            return Integer.toString(relay.getId()) + ". " + relay.getName();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return "Unreachable Relay";
        }
    }
    
    public IRemoteTrafficRelay getRelay(int index)
    {
        return relays[index];
    }
    
    public void fireUpdated(IRemoteTrafficRelay[] relays)
    {
        this.relays = relays;
        this.fireContentsChanged(this, 0, getSize());
    }

}
