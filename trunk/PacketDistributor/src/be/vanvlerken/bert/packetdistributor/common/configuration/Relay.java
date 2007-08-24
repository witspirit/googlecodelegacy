/**
 * @author wItspirit
 * 25-dec-2004
 * Relay.java
 */

package be.vanvlerken.bert.packetdistributor.common.configuration;

import java.util.LinkedList;
import java.util.List;


/**
 * Wraps the configuration of a Relay
 */
public class Relay
{
    private String Name;
    private List<Endpoint> Sources;
    private List<Endpoint> Destinations;
    
    public Relay()
    {
        Name = null;
        Sources = new LinkedList<Endpoint>();
        Destinations = new LinkedList<Endpoint>();
    }
    
    public Relay(String name)
    {
        Name = name;
        Sources = new LinkedList<Endpoint>();
        Destinations = new LinkedList<Endpoint>();
    }
    
    private void initSources()
    {
        if ( Sources == null )
        {
            Sources = new LinkedList<Endpoint>();
        }
    }
    
    private void initDestinations()
    {
        if ( Destinations == null )
        {
            Destinations = new LinkedList<Endpoint>();
        }
    }
    
    /**
     * @return Returns the destinations.
     */
    public List<Endpoint> getDestinations()
    {
        initDestinations();
        return Destinations;
    }
    
    public void addDestination(Endpoint destination)
    {
        initDestinations();
        Destinations.add(destination);
    }
    
    public void removeDestination(Endpoint destination)
    {
        initDestinations();
        Destinations.remove(destination);        
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return Name;
    }
    /**
     * @return Returns the sources.
     */
    public List<Endpoint> getSources()
    {
        initSources();
        return Sources;
    }
    
    public void addSource(Endpoint source)
    {
        initSources();
        Sources.add(source);
    }
    
    public void removeSource(Endpoint source)
    {
        initSources();
        Sources.remove(source);
    }
}
