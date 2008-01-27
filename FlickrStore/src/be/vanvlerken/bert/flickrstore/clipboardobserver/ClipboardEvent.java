package be.vanvlerken.bert.flickrstore.clipboardobserver;

public class ClipboardEvent {
    private final Object source;
    private final Object data;
    
    public ClipboardEvent(Object source, Object data) {
	this.source = source;
	this.data = data;
    }

    public Object getSource() {
        return source;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }
}
