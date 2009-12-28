package growl.delegate.growltalk;

public enum GrowlTalkPacketType {
	REGISTRATION(0),
	NOTIFICATION(1),
	REGISTRATION_SHA256(2),
	NOTIFICATION_SHA256(3),
	REGISTRATION_NOAUTH(4),
	NOTIFICATION_NOAUTH(5);

	private int typeId;
	
	private GrowlTalkPacketType(int typeId) {
		this.typeId = typeId;
	}
	
	public int getTypeId() {
		return typeId;
	}
}
