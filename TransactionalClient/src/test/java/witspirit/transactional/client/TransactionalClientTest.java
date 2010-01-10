package witspirit.transactional.client;

import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;


public class TransactionalClientTest {

    @Test
    public void immediatelyGood() {
	TransactionalClient<TestRequest> client = new TransactionalClient<TestRequest>(new Configuration<TestRequest>(new RequestHandler<TestRequest>() {

	    @Override
	    public String doSendRequest(TestRequest request, boolean isRepeat) throws TimeoutException {
		return "dummyId";
	    }

	    @Override
	    public void doSendReversal(TestRequest request, String transactionId) throws TimeoutException {
		
	    }

	    @Override
	    public void transactionDone(TestRequest request, String transactionId, TransactionStatus status) {
		Assert.assertEquals("dummyId", transactionId);
		Assert.assertEquals(TransactionStatus.SUCCESS, status);
	    }

	    
	}));
	TestRequest request = new TestRequest("immediatelyGood");
	Transaction<TestRequest> transaction = client.send(request);
	Assert.assertEquals("dummyId", transaction.getTransactionId());
	Assert.assertEquals(request, transaction.getRequest());
    }
    
    @Test
    public void goodAfterRetry() {
	TransactionalClient<TestRequest> client = new TransactionalClient<TestRequest>(new Configuration<TestRequest>(new RequestHandler<TestRequest>() {

	    @Override
	    public String doSendRequest(TestRequest request, boolean isRepeat) throws TimeoutException {
		if (isRepeat) {
		    return "dummyId";
		} else {
		    throw new TimeoutException();
		}
	    }

	    @Override
	    public void doSendReversal(TestRequest request, String transactionId) throws TimeoutException {
		
	    }

	    @Override
	    public void transactionDone(TestRequest request, String transactionId, TransactionStatus status) {
		Assert.assertEquals("dummyId", transactionId);
		Assert.assertEquals(TransactionStatus.SUCCESS, status);
	    }

	    
	}));
	TestRequest request = new TestRequest("goodAfterRetry");
	Transaction<TestRequest> transaction = client.send(request);
	Assert.assertEquals("dummyId", transaction.getTransactionId());
	Assert.assertEquals(request, transaction.getRequest());
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
}
