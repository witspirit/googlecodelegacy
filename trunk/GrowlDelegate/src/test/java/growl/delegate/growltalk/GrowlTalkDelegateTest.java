package growl.delegate.growltalk;

import org.junit.Test;


public class GrowlTalkDelegateTest {

	@Test
	public void sendSimpleNotification() {
		GrowlTalkDelegate growl = new GrowlTalkDelegate("Growl Delegate Library");
		Notification basicNotification = growl.addNotificationType(ExampleNotificationType.BASIC);
		Notification otherNotification = growl.addNotificationType(ExampleNotificationType.OTHER);
		growl.register();
		
		growl.notify(ExampleNotificationType.BASIC, "Test Message", "Basic Notification Test Message via delegate");
		growl.notify(ExampleNotificationType.OTHER, "Other Test Message", "Other Notification Test Message via delegate", GrowlTalkPriority.HIGH, true);
		
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
	
	@Test
	public void withMD5Password() {
	    GrowlTalkDelegate growl = new GrowlTalkDelegate("Growl Delegate Library", GrowlSecurity.MD5.authentication("witspiritrules"));
	    Notification n = growl.addNotificationType("MD5 Message", true);
	    growl.register();
	    
	    n.send("Password Message", "MD5 Password protected message");
	}
	
	@Test
    public void withSHA256Password() {
        GrowlTalkDelegate growl = new GrowlTalkDelegate("Growl Delegate Library", GrowlSecurity.SHA256.authentication("witspiritrules"));
        Notification n = growl.addNotificationType("SHA256 Message", true);
        growl.register();
        
        n.send("Password Message", "SHA256 Password protected message");
    }
}
