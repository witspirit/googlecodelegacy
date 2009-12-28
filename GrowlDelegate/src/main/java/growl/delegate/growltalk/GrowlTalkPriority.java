package growl.delegate.growltalk;

public enum GrowlTalkPriority {
	VERY_HIGH(2),
	HIGH(1),
	NORMAL(0),
	LOW(-1),
	VERY_LOW(-2);
	
	private int priorityCode;
	
	private GrowlTalkPriority(int priorityCode) {
		this.priorityCode = priorityCode;
	}
	
	public int getPriorityCode() {
		return priorityCode;
	}
}
