/**
 * @author wItspirit
 * 13-mei-2003
 * SelectionEventObject.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.frontend;

import java.util.EventObject;

import be.vanvlerken.bert.packetdistributor.common.rmi.IRemoteTrafficRelay;

/**
 * Transport object for a Selection Event
 */
public class SelectionEventObject extends EventObject
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3976741376001979442L;
    private IRemoteTrafficRelay relay;
    
    public SelectionEventObject(Object source, IRemoteTrafficRelay relay)
    {
        super(source);
        this.relay = relay;
    }
    
    /**
     * @return
     */
    public IRemoteTrafficRelay getRelay()
    {
        return relay;
    }

}
