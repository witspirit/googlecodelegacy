/**
 * @author wItspirit
 * 20-apr-2003
 * EventSupport.java
 */
package be.vanvlerken.bert.components.eventsupport;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

/**
 * Provides basic support for events.
 * It holds the list of subscribed users and can dispatch an event to all subscribers
 */
public class EventSupport
{
    private List                subscribers;
    private EventDispatcher     eventDispatcher;
    
    public EventSupport(EventDispatcher eventDispatcher)
    {
        subscribers = new ArrayList();
        this.eventDispatcher = eventDispatcher;
    }
    
    public void addListener(EventListener listener)
    {
        subscribers.add(listener);
    }
    
    public void removeListener(EventListener listener)
    {
        subscribers.remove(listener);
    }
    
    public void fireEvent(EventObject eo)
    {
        List safeList = new ArrayList(subscribers);
        for (int i=0; i < safeList.size(); i++)
        {
            EventListener listener = (EventListener) safeList.get(i);
            eventDispatcher.dispatch(listener, eo);
        }
    }
}
