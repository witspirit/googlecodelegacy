package growl.delegate.growltalk;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegistrationPacket implements GrowlPacket {
	private GrowlTalkVersion version;
	private GrowlTalkPacketType type;
	private String applicationName;
	private Map<String, Boolean> notifications = new LinkedHashMap<String, Boolean>();
	
	public RegistrationPacket(GrowlTalkVersion version, GrowlTalkPacketType type, String applicationName) {
		this.version = version;
		this.type = type;
		this.applicationName = applicationName;
	}
	
	public void addNotificationType(String notificationType, boolean defaultEnabled) {
		notifications.put(notificationType, defaultEnabled);
	}
	
	public GrowlTalkVersion getVersion() {
		return version;
	}
	
	public GrowlTalkPacketType getType() {
		return type;
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
		daos.writeByte(version.getVersionId());
		daos.writeByte(type.getTypeId());

		byte[] applicationNameBytes = applicationName.getBytes("UTF-8");
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
		
		switch (type) {
		case NOTIFICATION:
		case REGISTRATION:
			// MD5 Checksum
			throw new UnsupportedOperationException("Don't support MD5 checksum");
		case NOTIFICATION_SHA256:
		case REGISTRATION_SHA256:
			// SHA256 Checksum
			throw new UnsupportedOperationException("Don't support SHA256 checksum");
		case NOTIFICATION_NOAUTH:
		case REGISTRATION_NOAUTH:
			// No Checksum
			break;
		default:
			throw new UnsupportedOperationException("Unknown Checksum strategy for "+type);
		}
		daos.flush();
		
		return baos.toByteArray();
	}
}
