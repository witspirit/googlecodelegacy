package witspirit.transactional.client;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;


public class ConfigurationTest {
    
    @Test
    public void defaultConfiguration() {
	RequestHandler<Object> requestHandler = new DummyRequestHandler();
	TransactionRecorder<Object> transactionRecorder = new DummyTransactionRecorder();
	Configuration<Object> configuration = new Configuration<Object>(requestHandler, transactionRecorder);
	
	Assert.assertEquals(requestHandler, configuration.getRequestHandler());
	Assert.assertEquals(transactionRecorder, configuration.getTransactionRecorder());
	Assert.assertEquals(3, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(4, configuration.getMaximumReversalAttempts());
    }
    
    @Test
    public void customInitializedConfiguration() {
	RequestHandler<Object> requestHandler = new DummyRequestHandler();
	TransactionRecorder<Object> transactionRecorder = new DummyTransactionRecorder();
	Configuration<Object> configuration = new Configuration<Object>(requestHandler, transactionRecorder, Arrays.asList(1,2,3,4,5), Arrays.asList(60));
	
	Assert.assertEquals(requestHandler, configuration.getRequestHandler());
	Assert.assertEquals(transactionRecorder, configuration.getTransactionRecorder());
	Assert.assertEquals(5, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(1, configuration.getMaximumReversalAttempts());
    }
    
    @Test
    public void customConfiguration() {
	RequestHandler<Object> requestHandler = new DummyRequestHandler();
	TransactionRecorder<Object> transactionRecorder = new DummyTransactionRecorder();
	Configuration<Object> configuration = new Configuration<Object>(requestHandler, transactionRecorder);
	configuration.setRepeatIntervals(1,2,3,4,5).setReversalIntervals(60);
	
	Assert.assertEquals(requestHandler, configuration.getRequestHandler());
	Assert.assertEquals(transactionRecorder, configuration.getTransactionRecorder());
	Assert.assertEquals(5, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(1, configuration.getMaximumReversalAttempts());
    }
    
    @Test
    public void protectionAgainstNulls() {
	DummyRequestHandler requestHandler = new DummyRequestHandler();
	TransactionRecorder<Object> transactionRecorder = new DummyTransactionRecorder();

	Configuration<Object> configuration;
	try {
	    configuration = new Configuration<Object>(null, null);
	    Assert.fail("Expected NullPointerException for missing RequestHandler");
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().contains("RequestHandler"));
	}
	
	try {
	    configuration = new Configuration<Object>(requestHandler, null);
	    Assert.fail("Expected NullPointerException for missing TransactionRecorder");
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().contains("TransactionRecorder"));
	}
	
	configuration = new Configuration<Object>(requestHandler, transactionRecorder, null, null);
	Assert.assertEquals(0, configuration.getMaximumRepeatAttempts());
	Assert.assertEquals(0, configuration.getMaximumReversalAttempts());
	
	configuration = new Configuration<Object>(requestHandler, transactionRecorder);
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
	
    }
    
    private class DummyTransactionRecorder implements TransactionRecorder<Object> {

	@Override
	public void transactionDone(Object request, String transactionId, TransactionStatus status) {
	    
	}
	
    }

}
