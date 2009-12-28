package growl.delegate.growltalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import org.junit.Test;


/**
 * Test requires an operational Growl setup
 */
public class RegistrationTest {

	@Test
	public void sendSimpleRegistration() throws IOException {
		DatagramSocket socket = new DatagramSocket();
		
		RegistrationPacket registrationPacket = new RegistrationPacket(GrowlTalkVersion.PLAIN, GrowlTalkPacketType.REGISTRATION_NOAUTH, "Growl Delegate Library");
		registrationPacket.addNotificationType("Basic Notification", true);
		byte[] buf = registrationPacket.asMessageBytes();
		
		System.out.println(Arrays.toString(buf));
		
		DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName("127.0.0.1"), 9887);
		
		socket.send(packet);
	}
}
