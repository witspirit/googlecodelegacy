package growl.delegate.growltalk;

public interface Notification {
	NotificationType getType();
	
	void send(String title, String description);
	
	void send(String title, String description, GrowlTalkPriority priority, boolean sticky);
}
