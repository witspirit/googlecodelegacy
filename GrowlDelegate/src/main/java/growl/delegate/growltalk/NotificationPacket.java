package growl.delegate.growltalk;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NotificationPacket implements GrowlPacket {
	private GrowlTalkVersion version;
	private GrowlTalkPacketType type;
	private String applicationName;
	private GrowlTalkPriority priority;
	private boolean sticky;
	private String notificationType;
	private String title;
	private String description;

	public NotificationPacket(GrowlTalkVersion version,
			GrowlTalkPacketType type, String applicationName,
			GrowlTalkPriority priority, boolean sticky,
			String notificationType, String title, String description) {
		this.version = version;
		this.type = type;
		this.applicationName = applicationName;
		this.priority = priority;
		this.sticky = sticky;
		this.notificationType = notificationType;
		this.title = title;
		this.description = description;
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

	public GrowlTalkPriority getPriority() {
		return priority;
	}
	
	public boolean isSticky() {
		return sticky;
	}
	
	public String getNotificationType() {
		return notificationType;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}

	public byte[] asMessageBytes() throws IOException {
		byte[] applicationNameBytes = applicationName.getBytes("UTF-8");
		byte[] notificationTypeBytes = notificationType.getBytes("UTF-8");
		byte[] titleBytes = title.getBytes("UTF-8");
		byte[] descriptionBytes = description.getBytes("UTF-8");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.writeByte(version.getVersionId());
		daos.writeByte(type.getTypeId());
		
		daos.writeShort(deriveFlags());
		
		daos.writeShort(notificationTypeBytes.length);
		daos.writeShort(titleBytes.length);
		daos.writeShort(descriptionBytes.length);
		daos.writeShort(applicationNameBytes.length);
		
		daos.write(notificationTypeBytes);
		daos.write(titleBytes);
		daos.write(descriptionBytes);
		daos.write(applicationNameBytes);

		switch (type) {
		case NOTIFICATION:
		case REGISTRATION:
			// MD5 Checksum
			throw new UnsupportedOperationException(
					"Don't support MD5 checksum");
		case NOTIFICATION_SHA256:
		case REGISTRATION_SHA256:
			// SHA256 Checksum
			throw new UnsupportedOperationException(
					"Don't support SHA256 checksum");
		case NOTIFICATION_NOAUTH:
		case REGISTRATION_NOAUTH:
			// No Checksum
			break;
		default:
			throw new UnsupportedOperationException("Unknown Checksum strategy for " + type);
		}
		daos.flush();

		return baos.toByteArray();
	}
	
	private int deriveFlags() {
	    int flags = (priority.getPriorityCode() & 0x7) << 9;
        if (sticky) {
            flags |= 0x100;
        }
        return flags;
	}
}
