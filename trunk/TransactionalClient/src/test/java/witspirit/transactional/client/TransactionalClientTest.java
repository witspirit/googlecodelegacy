package witspirit.transactional.client;

import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;


public class TransactionalClientTest {

    @Test
    public void singleGoodRequest() {
	TransactionalClient<Object> client = new TransactionalClient<Object>(new Configuration<Object>(new RequestHandler<Object>() {

	    @Override
	    public String doSendRequest(Object request, boolean isRepeat) throws TimeoutException {
		return "dummyId";
	    }

	    @Override
	    public void doSendReversal(Object request, String transactionId) throws TimeoutException {
		
	    }

	    @Override
	    public void transactionDone(Object request, String transactionId, TransactionStatus status) {
		Assert.assertEquals("dummyId", transactionId);
		Assert.assertEquals(TransactionStatus.SUCCESS, status);
	    }

	    
	}));
	Transaction transaction = client.send(new Object());
    }
}
