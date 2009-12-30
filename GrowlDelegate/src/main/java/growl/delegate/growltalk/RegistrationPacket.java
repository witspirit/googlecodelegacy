package growl.delegate.growltalk;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegistrationPacket implements GrowlPacket {
    private GrowlAuthentication auth;
	private String applicationName;
	private Map<String, Boolean> notifications = new LinkedHashMap<String, Boolean>();
	
	public RegistrationPacket(GrowlAuthentication auth, String applicationName) {
	    this.auth = auth;
		this.applicationName = applicationName;
	}
	
	public void addNotificationType(String notificationType, boolean defaultEnabled) {
		notifications.put(notificationType, defaultEnabled);
	}
	
	public GrowlTalkVersion getVersion() {
		return auth.getProtocolVersion();
	}
	
	public GrowlTalkPacketType getType() {
		return auth.getRegistrationType();
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	
	public Map<String, Boolean> getNotifications() {
		return notifications;
	}
	
	public byte[] asMessageBytes() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.writeByte(auth.getProtocolVersion().getVersionId());
		daos.writeByte(auth.getRegistrationType().getTypeId());

		byte[] applicationNameBytes = Util.toUTF8(applicationName);
		daos.writeShort(applicationNameBytes.length);
		
		daos.writeByte(notifications.size());
		
		List<Integer> defaultIndexes = new ArrayList<Integer>();
		int i=0;
		for (Map.Entry<String, Boolean> entry : notifications.entrySet()) {
			if (entry.getValue()) {
				defaultIndexes.add(i);
			}
			i++;
		}
		daos.writeByte(defaultIndexes.size());
		
		daos.write(applicationNameBytes);
		
		for (String notificationType : notifications.keySet()) {
			daos.writeUTF(notificationType);
		}
		
		for (int defIndex : defaultIndexes) {
			daos.writeByte(defIndex);
		}
		daos.flush();
		byte[] message = baos.toByteArray();
		
		daos.write(auth.generateChecksum(message));
		
		daos.flush();
		return baos.toByteArray();
	}
}
