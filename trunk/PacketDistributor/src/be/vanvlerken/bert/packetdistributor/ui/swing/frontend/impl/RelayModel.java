/**
 * @author wItspirit
 * 27-apr-2003
 * RelayModel.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import java.rmi.RemoteException;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.ListModel;

import be.vanvlerken.bert.components.eventsupport.EventDispatcher;
import be.vanvlerken.bert.components.eventsupport.EventSupport;
import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.AddRemoveModel;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.SelectionEventObject;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.SelectionListener;

/**
 * The model for displaying DummyTrafficRelay information
 */
public class RelayModel implements AddRemoveModel, EventDispatcher
{
    private RelayListModel           listModel;
    private RelayJComponent          inputComponent;

    private IPacketDistributorFacade pd;

    private EventSupport             eventSupport;

    public RelayModel(IPacketDistributorFacade pd)
    {
        this.pd = pd;

        try
        {
            listModel = new RelayListModel(pd.getRelays());
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

        eventSupport = new EventSupport(this);

        inputComponent = new RelayJComponent();
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getTitle()
     */
    public String getTitle()
    {
        return "Relays";
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getElementName()
     */
    public String getElementName()
    {
        return "TrafficRelay";
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#getListModel()
     */
    public ListModel getListModel()
    {
        return listModel;
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#remove(int)
     */
    public void remove(int selectedItem)
    {
        try
        {
            pd.destroyRelay(listModel.getRelay(selectedItem));
            listModel.fireUpdated(pd.getRelays());
            inputComponent.clearFields();

            eventSupport.fireEvent(new SelectionEventObject(this, null));

        }
        catch (RemoteException e)
        {
            // @TODO Should show error dialog
            e.printStackTrace();
        }
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#add()
     */
    public void add()
    {
        try
        {
            pd.createRelay(inputComponent.getName());            
            listModel.fireUpdated(pd.getRelays());
            inputComponent.clearFields();
        }
        catch (RemoteException e)
        {
            // @TODO Should show error dialog
            e.printStackTrace();
        }
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
        if (selectedItem == -1)
        {
            inputComponent.clearFields();
            return;
        }

        IRemoteTrafficRelay relay = listModel.getRelay(selectedItem);
        try
        {
            inputComponent.setId(relay.getId());
            inputComponent.setName(relay.getName());
            eventSupport.fireEvent(new SelectionEventObject(this, relay));
        }
        catch (RemoteException e)
        {
            // @TODO Should show an error dialog
            e.printStackTrace();
        }
    }

    public void addSelectionListener(SelectionListener listener)
    {
        eventSupport.addListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener)
    {
        eventSupport.removeListener(listener);
    }

    /**
     * @see be.vanvlerken.bert.components.eventsupport.EventDispatcher#dispatch(java.util.EventListener,
     *      java.util.EventObject)
     */
    public void dispatch(EventListener listener, EventObject eo)
    {
        SelectionListener target = (SelectionListener) listener;
        SelectionEventObject seo = (SelectionEventObject) eo;

        target.selected(seo);
    }

    /**
     * @see be.vanvlerken.bert.packetdistributor.ui.console.gui.AddRemoveModel#isActive()
     */
    public boolean isActive()
    {
        return true;
    }

    public void cleanUp()
    {
        pd.shutdown();
    }

}
