package witspirit.transactional.client;

import java.util.concurrent.TimeoutException;

import witspirit.transactional.client.fsm.SendingRequestState;
import witspirit.transactional.client.fsm.TransactionState;

public class TransactionalClient<REQUEST> {
    private Configuration<REQUEST> configuration;
    
    public TransactionalClient(Configuration<REQUEST> configuration) {
	this.configuration = configuration;
    }
    
    public Transaction<REQUEST> send(REQUEST request) {
	TransactionImpl transaction = new TransactionImpl(request);
	try {
	    String transactionId = configuration.getRequestHandler().doSendRequest(request, false);
	    transaction.setTransactionId(transactionId);
	    configuration.getRequestHandler().transactionDone(request, transaction.getTransactionId(), TransactionStatus.SUCCESS);
	} catch (TimeoutException e) {
	    configuration.getRequestHandler().transactionDone(request, transaction.getTransactionId(), TransactionStatus.FAILURE);
	}
	return transaction;
    }
    
    private class TransactionImpl implements Transaction<REQUEST> {
	private final REQUEST request;
	private String transactionId = null;
	private TransactionState state;
	
	public TransactionImpl(REQUEST request) {
	    this.request = request;
	    setState(new SendingRequestState<REQUEST>(request, configuration));
	}
	
	private void setState(TransactionState state) {
	    if (this.state != state) {
		this.state = state;
		setState(this.state.activate());
	    }
	}
	
	public String getTransactionId() {
	    return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
	    this.transactionId = transactionId;
	}

	@Override
	public void abort() {
	    try {
		configuration.getRequestHandler().doSendReversal(request, transactionId);
		configuration.getRequestHandler().transactionDone(request, transactionId, TransactionStatus.FAILURE);
	    } catch (TimeoutException e) {
		configuration.getRequestHandler().transactionDone(request, transactionId, TransactionStatus.INTERVENTION_REQUIRED);
	    }
	}

	@Override
	public REQUEST getRequest() {
	    return request;
	}
    }
    

}
