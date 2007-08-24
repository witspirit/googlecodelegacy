/**
 * @author wItspirit
 * 25-dec-2004
 * ConfigurationTester.java
 */

package be.vanvlerken.bert.packetdistributor.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import be.vanvlerken.bert.packetdistributor.common.configuration.Endpoint;
import be.vanvlerken.bert.packetdistributor.common.configuration.PDConfig;
import be.vanvlerken.bert.packetdistributor.common.configuration.Relay;

/**
 * This test case covers the tests to verify the succesful operation of XStream
 * in our system.
 */
public class ConfigurationTester extends TestCase
{
    public void testXmlOutput()
    {
        try
        {
            PDConfig config = new PDConfig(
                    "be.vanvlerken.bert.packetdistributor.common.impl.dummy");
            Relay relay = new Relay("Test");
            Endpoint e1 = new Endpoint("0.0.0.0", 10000, "UDP");
            Endpoint e2 = new Endpoint("127.0.0.1", 10001, "UDP");

            relay.addSource(e1);
            relay.addDestination(e2);
            config.addRelay(relay);

            config.writeXml(new FileOutputStream("tests/testXmlOutput.xml"));
        }        
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
