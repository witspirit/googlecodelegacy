package growl.delegate.growltalk;

public enum TestNotificationType implements NotificationType {
	BASIC("Basic Notification", true),
	OTHER("Other Notification", true);
	
	private String name;
	private boolean enabledByDefault;
	
	private TestNotificationType(String name, boolean enabledByDefault) {
		this.name = name;
		this.enabledByDefault = enabledByDefault;
	}

	public String getName() {
		return name;
	}

	public boolean isEnabledByDefault() {
		return enabledByDefault;
	}
}
