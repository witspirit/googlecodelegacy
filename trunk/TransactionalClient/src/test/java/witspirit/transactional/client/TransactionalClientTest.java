package witspirit.transactional.client;

import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;


public class TransactionalClientTest {

    @Test
    public void immediatelyGood() {
	TransactionalClient<TestRequest> client = client(new TestRequestHandler(false, false), new TestRecorder(TestRequestHandler.DEFAULT_TXID, TransactionStatus.SUCCESS));
	TestRequest request = new TestRequest("immediatelyGood");
	Transaction<TestRequest> transaction = client.send(request);
	Assert.assertEquals(TestRequestHandler.DEFAULT_TXID, transaction.getTransactionId());
	Assert.assertEquals(request, transaction.getRequest());
    }
    
    @Test
    public void goodAfterRetry() {
	TransactionalClient<TestRequest> client = client(new TestRequestHandler(false, false) {

	    @Override
	    public String doSendRequest(TestRequest request, boolean isRepeat) throws TimeoutException {
		if (isRepeat) {
		    return TestRequestHandler.DEFAULT_TXID;
		} else {
		    throw new TimeoutException();
		}
	    }

	}, new TestRecorder(TestRequestHandler.DEFAULT_TXID, TransactionStatus.SUCCESS));
	TestRequest request = new TestRequest("goodAfterRetry");
	Transaction<TestRequest> transaction = client.send(request);
	Assert.assertEquals(TestRequestHandler.DEFAULT_TXID, transaction.getTransactionId());
	Assert.assertEquals(request, transaction.getRequest());
    }
    
    @Test
    public void goodReversalAfterTimeouts() {
	TransactionalClient<TestRequest> client = client(new TestRequestHandler(true, false), new TestRecorder(null, TransactionStatus.FAILURE));
	TestRequest request = new TestRequest("goodReversalAfterTimeouts");
	Transaction<TestRequest> transaction = client.send(request);
	Assert.assertNull(transaction.getTransactionId());
	Assert.assertEquals(request, transaction.getRequest());
    }
    
    @Test
    public void timeoutsOnAll() {
	TransactionalClient<TestRequest> client = client(new TestRequestHandler(true, true), new TestRecorder(null, TransactionStatus.INTERVENTION_REQUIRED));
	TestRequest request = new TestRequest("timeoutsOnAll");
	Transaction<TestRequest> transaction = client.send(request);
	Assert.assertNull(transaction.getTransactionId());
	Assert.assertEquals(request, transaction.getRequest());
    }
    
    private TransactionalClient<TestRequest> client(RequestHandler<TestRequest> requestHandler, TransactionRecorder<TestRequest> transactionRecorder) {
	return new TransactionalClient<TestRequest>(new Configuration<TestRequest>(requestHandler, transactionRecorder));
    }
    
    private class TestRequest {
	private String name;
	
	public TestRequest(String name) {
	    this.name = name;
	}
	
	@Override
	public String toString() {
	    return name;
	}
    }
    
    private class TestRecorder implements TransactionRecorder<TestRequest> {
	private String expectedTransactionId;
	private TransactionStatus expectedStatus;
	
	public TestRecorder(String expectedTransactionId, TransactionStatus expectedStatus) {
	    this.expectedTransactionId = expectedTransactionId;
	    this.expectedStatus = expectedStatus;
	}
	

	@Override
	public void transactionDone(TestRequest request, String transactionId, TransactionStatus status) {
	    Assert.assertEquals(expectedTransactionId, transactionId);
	    Assert.assertEquals(expectedStatus, status);
	}
    }
    
    private class TestRequestHandler implements RequestHandler<TestRequest> {
	public static final String DEFAULT_TXID = "dummyId";
	private boolean requestTimeout;
	private boolean reversalTimeout;
	
	public TestRequestHandler(boolean requestTimeout, boolean reversalTimeout) {
	    this.requestTimeout = requestTimeout;
	    this.reversalTimeout = reversalTimeout;
	}

	@Override
	public String doSendRequest(TestRequest request, boolean isRepeat) throws TimeoutException {
	    if (requestTimeout) {
		throw new TimeoutException();
	    }
	    return DEFAULT_TXID;
	}

	@Override
	public void doSendReversal(TestRequest request, String transactionId) throws TimeoutException {
	    if (reversalTimeout) {
		throw new TimeoutException();
	    }
	}
	
    }
}
