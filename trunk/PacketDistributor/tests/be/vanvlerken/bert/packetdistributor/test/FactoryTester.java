/**
 * @author wItspirit
 * 24-dec-2004
 * FactoryTester.java
 */

package be.vanvlerken.bert.packetdistributor.test;

import java.util.Collection;
import java.util.Iterator;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.configuration.PacketDistributorFactory;
import be.vanvlerken.bert.packetdistributor.common.impl.dummy.PdDummyInterface;
import junit.framework.TestCase;


/**
 * Tests the PacketDistributorFactory
 */
public class FactoryTester extends TestCase
{

    /*
     * Class under test for void PacketDistributorFactory()
     */
    public void testPacketDistributorFactory()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory();
        assertNotNull(factory);
    }

    /*
     * Class under test for void PacketDistributorFactory(String)
     */
    public void testPacketDistributorFactoryString()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory("packetdistributor.xml");
        assertNotNull(factory);
    }

    public void testGetPacketDistributorAPI()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory();
        assertNotNull(factory);
        IPacketDistributorAPI api = factory.getPacketDistributorAPI();
        assertNotNull(api);
    }
    
    public void testApiType()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory("testApi.xml");
        assertNotNull(factory);
        IPacketDistributorAPI api = factory.getPacketDistributorAPI();
        assertNotNull(api);
        assertTrue(api instanceof PdDummyInterface);
    }

    public void testTypicalConfig()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory("testApi.xml");
        IPacketDistributorAPI api = factory.getPacketDistributorAPI();
        Collection<ITrafficRelay> relays = api.getRelays();
        assertTrue(relays.size() == 1);
        Iterator<ITrafficRelay> it = relays.iterator();
        ITrafficRelay relay = it.next();
        assertEquals(relay.getName(), "Test");
        ITrafficSource[] sources = relay.getSources();
        assertTrue(sources.length == 1);
        ITrafficDestination[] destinations = relay.getDestinations();
        assertTrue(destinations.length == 1);
        
        verifyEndpoint(sources[0],"0.0.0.0",10000);
        verifyEndpoint(destinations[0],"127.0.0.1",10001);
        
    }
    
    public void testEmptyConfig()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory("testEmpty.xml");
        IPacketDistributorAPI api = factory.getPacketDistributorAPI();
        Collection<ITrafficRelay> relays = api.getRelays();
        assertTrue(relays.size() == 0);
    }
    
    public void testUnknownXmlFile()
    {
        PacketDistributorFactory factory = new PacketDistributorFactory("noSuchFile.xml");
        assertNotNull(factory);
        IPacketDistributorAPI api = factory.getPacketDistributorAPI();
        assertNotNull(api);
        assertTrue(api instanceof PdDummyInterface);
    }

    /**
     * @param sources
     */
    private void verifyEndpoint(ITrafficEndpoint endpoint, String ip, int port)
    {
        assertEquals(endpoint.getIpAddress().getHostAddress(), ip);
        assertEquals(endpoint.getPort(), port);
        assertTrue(endpoint.getProtocol() == ITrafficEndpoint.UDP);
    }
    
}
