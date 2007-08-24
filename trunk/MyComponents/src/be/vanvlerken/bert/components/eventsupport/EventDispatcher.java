/**
 * @author wItspirit
 * 20-apr-2003
 * EventDispatcher.java
 */
package be.vanvlerken.bert.components.eventsupport;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interface for dispatching a generic event to a specific method
 */
public interface EventDispatcher
{
    /**
     * Method dispatch.
     * @param listener
     * @param eo
     */
    public void dispatch(EventListener listener, EventObject eo);
}
