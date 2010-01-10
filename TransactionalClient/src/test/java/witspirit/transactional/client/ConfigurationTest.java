package witspirit.transactional.client;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;


public class ConfigurationTest {
    
    @Test
    public void defaultConfiguration() {
	RequestHandler<Object> requestHandler = new DummyRequestHandler();
	Configuration<Object> configuration = new Configuration<Object>(requestHandler);
	
	Assert.assertEquals(requestHandler, configuration.getRequestHandler());
	Assert.assertEquals(3, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(4, configuration.getMaximumReversalAttempts());
    }
    
    @Test
    public void customInitializedConfiguration() {
	RequestHandler<Object> requestHandler = new DummyRequestHandler();
	Configuration<Object> configuration = new Configuration<Object>(requestHandler, Arrays.asList(1,2,3,4,5), Arrays.asList(60));
	
	Assert.assertEquals(requestHandler, configuration.getRequestHandler());
	Assert.assertEquals(5, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(1, configuration.getMaximumReversalAttempts());
    }
    
    @Test
    public void customConfiguration() {
	RequestHandler<Object> requestHandler = new DummyRequestHandler();
	Configuration<Object> configuration = new Configuration<Object>(requestHandler);
	configuration.setRepeatIntervals(1,2,3,4,5).setReversalIntervals(60);
	
	Assert.assertEquals(requestHandler, configuration.getRequestHandler());
	Assert.assertEquals(5, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(1, configuration.getMaximumReversalAttempts());
    }
    
    @Test
    public void protectionAgainstNulls() {
	Configuration<Object> configuration;
	try {
	    configuration = new Configuration<Object>(null);
	    Assert.fail("Expected NullPointerException for missing RequestHandler");
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().contains("RequestHandler"));
	}
	
	DummyRequestHandler requestHandler = new DummyRequestHandler();
	configuration = new Configuration<Object>(requestHandler, null, null);
	Assert.assertEquals(0, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(0, configuration.getMaximumReversalAttempts());
	
	configuration = new Configuration<Object>(requestHandler);
	configuration.setRepeatIntervals();
	configuration.setReversalIntervals();
	Assert.assertEquals(0, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(0, configuration.getMaximumReversalAttempts());
    }
    
    private class DummyRequestHandler implements RequestHandler<Object> {

	@Override
	public String doSendRequest(Object request, boolean isRepeat) throws TimeoutException {
	    return null;
	}

	@Override
	public void doSendReversal(Object request, String transactionId) throws TimeoutException {
	    
	}

	@Override
	public void transactionDone(Object request, String transactionId, TransactionStatus status) {
	    
	}
	
    }

}
