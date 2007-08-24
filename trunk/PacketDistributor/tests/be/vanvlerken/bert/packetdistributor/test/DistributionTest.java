/**
 * @author wItspirit
 * 22-dec-2004
 * DistributionTest.java
 */

package be.vanvlerken.bert.packetdistributor.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import junit.framework.TestCase;


/**
 * This test case will verify that a certain connection has been established
 */
public class DistributionTest extends TestCase
{
    public void testDistribution()
    {
        DatagramSocket socket = null;
        try
        {
            socket = new DatagramSocket(10001);
            ByteArrayOutputStream transmitPayload = new ByteArrayOutputStream();
            DataOutputStream message = new DataOutputStream(transmitPayload);
            message.writeUTF("This is a test message from testDistribution");
            message.flush();
            DatagramPacket sendPacket = new DatagramPacket(transmitPayload.toByteArray(),transmitPayload.size(), InetAddress.getByName("127.0.0.1"), 10000);
            socket.send(sendPacket);
            DatagramPacket receivedPacket = new DatagramPacket(new byte[100], 100);
            socket.setSoTimeout(5000);
            socket.setReuseAddress(true);
            socket.receive(receivedPacket);
            ByteArrayInputStream receivedPayload = new ByteArrayInputStream(receivedPacket.getData());
            DataInputStream receivedMessage = new DataInputStream(receivedPayload);
            String testMessage = receivedMessage.readUTF();
            assertEquals("This is a test message from testDistribution", testMessage);            
        }
        catch (SocketException e)
        {            
            fail(e.getMessage());
        }
        catch (IOException e)
        {            
            fail(e.getMessage());
        }
        finally
        {
            if ( socket != null )
            {
                socket.close();
            }
        }
    }
}
