package growl.delegate.growltalk;

import org.junit.Test;


public class GrowlTalkDelegateTest {

	@Test
	public void sendSimpleNotification() {
		GrowlTalkDelegate growl = new GrowlTalkDelegate("Growl Delegate Library");
		Notification basicNotification = growl.addNotificationType(TestNotificationType.BASIC);
		Notification otherNotification = growl.addNotificationType(TestNotificationType.OTHER);
		growl.register();
		
		growl.notify(TestNotificationType.BASIC, "Test Message", "Basic Notification Test Message via delegate");
		growl.notify(TestNotificationType.OTHER, "Other Test Message", "Other Notification Test Message via delegate", GrowlTalkPriority.HIGH, true);
		
		basicNotification.send("Test Message", "Basic notification test message via notification");
		otherNotification.send("Other Test Message", "Other notification test message via notification", GrowlTalkPriority.HIGH, true);
	}
	
	@Test
	public void priorities() {
	    GrowlTalkDelegate growl = new GrowlTalkDelegate("Growl Delegate Library");
	    Notification notification = growl.addNotificationType("PriorityMessage", true);
	    growl.register();
	    
	    notification.send("Prio -2", "Prio -2", GrowlTalkPriority.VERY_LOW, false);
	    notification.send("Prio -1", "Prio -1", GrowlTalkPriority.LOW, false);
	    notification.send("Prio  0", "Prio  0", GrowlTalkPriority.NORMAL, false);
	    notification.send("Prio +1", "Prio +1", GrowlTalkPriority.HIGH, false);
	    notification.send("Prio +2", "Prio +2", GrowlTalkPriority.VERY_HIGH, false);
	}
	
	@Test
	public void sticky() {
	    GrowlTalkDelegate growl = new GrowlTalkDelegate("Growl Delegate Library");
	    Notification sticky = growl.addNotificationType("StickyMessage", true);
	    growl.register();
	    
	    sticky.send("Sticky", "Sticky Message", GrowlTalkPriority.NORMAL, true);
	}
}
