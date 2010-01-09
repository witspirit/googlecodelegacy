package witspirit.transactional.client;

public class Configuration<REQUEST> {
    private RequestHandler<REQUEST> requestHandler;
    
    public Configuration(RequestHandler<REQUEST> requestHandler) {
	this.requestHandler = requestHandler;
    }
    
    public RequestHandler<REQUEST> getRequestHandler() {
	return requestHandler;
    }
}
