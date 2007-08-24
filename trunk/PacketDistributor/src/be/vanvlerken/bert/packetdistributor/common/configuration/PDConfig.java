/**
 * @author wItspirit
 * 25-dec-2004
 * PDConfig.java
 */

package be.vanvlerken.bert.packetdistributor.common.configuration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.XStream;


/**
 * Toplevel Configuration entry-point.
 */
public class PDConfig
{
    private String PacketDistributorImpl;
    private List<Relay> Relays;
    
    public static PDConfig getInstance(InputStream xml)
    {
        XStream xstream = new XStream();
        setAliases(xstream);
        
        return (PDConfig) xstream.fromXML(new InputStreamReader(xml));
    }

    public void writeXml(OutputStream xml)
    {
        XStream xstream = new XStream();
        setAliases(xstream);
        
        xstream.toXML(this, new OutputStreamWriter(xml));
    }
    
    /**
     * @param xstream
     */
    private static void setAliases(XStream xstream)
    {
        xstream.alias("PacketDistributor",PDConfig.class);
        xstream.alias("Relay", Relay.class);
        xstream.alias("Endpoint", Endpoint.class);
    }
    
    public PDConfig()
    {
        PacketDistributorImpl = null;
        Relays = new LinkedList<Relay>();
    }
    
    public PDConfig(String pdImpl)
    {
        PacketDistributorImpl = pdImpl;
        Relays = new LinkedList<Relay>();
    }
    
    /**
     * @return Returns the packetDistributorImpl.
     */
    public String getPacketDistributorImpl()
    {
        return PacketDistributorImpl;
    }
    
    /**
     * @return Returns the relays.
     */
    public List<Relay> getRelays()
    {
        initRelays();
        return Relays;
    }

    /**
     * 
     */
    private void initRelays()
    {
        if ( Relays == null )
        {
            Relays = new LinkedList<Relay>();
        }
    }
    
    public void addRelay(Relay relay)
    {
        initRelays();
        Relays.add(relay);
    }
    
    public void removeRelay(Relay relay)
    {
        initRelays();
        Relays.remove(relay);
    }
}
