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
public class NotificationTest {

	@Test
	public void sendSimpleNotification() throws IOException {
		DatagramSocket socket = new DatagramSocket();
		
		// First register - to ensure we can send the notification
		String applicationName = "Growl Delegate Library";
		RegistrationPacket registrationPacket = new RegistrationPacket(GrowlTalkVersion.PLAIN, GrowlTalkPacketType.REGISTRATION_NOAUTH, applicationName);
		String notificationType = "Basic Notification";
		registrationPacket.addNotificationType(notificationType, true);
		byte[] buf = registrationPacket.asMessageBytes();
		
		DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName("127.0.0.1"), 9887);
		socket.send(packet);
		
		NotificationPacket notificationPacket = new NotificationPacket(GrowlTalkVersion.PLAIN, GrowlTalkPacketType.NOTIFICATION_NOAUTH, applicationName, GrowlTalkPriority.NORMAL, false, notificationType, "Test Message", "This is a test message. Ain't it cool.");
		buf = notificationPacket.asMessageBytes();
		System.out.println(Arrays.toString(buf));
		
		packet = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName("127.0.0.1"), 9887);
		socket.send(packet);
		
	}
}
