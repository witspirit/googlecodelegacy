package witspirit.transactional.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import witspirit.transactional.client.fsm.IdleState;
import witspirit.transactional.client.fsm.SendingRequestState;
import witspirit.transactional.client.fsm.TransactionState;

public class TransactionalClient<REQUEST> {
    private static final Logger log = LoggerFactory.getLogger(TransactionalClient.class);
    private Configuration<REQUEST> configuration;
    
    public TransactionalClient(Configuration<REQUEST> configuration) {
	this.configuration = configuration;
    }
    
    public Transaction<REQUEST> send(REQUEST request) {
	return new TransactionImpl(request);
    }
    
    private class TransactionImpl implements Transaction<REQUEST> {
	private final REQUEST request;
	private TransactionState state;
	
	public TransactionImpl(REQUEST request) {
	    this.request = request;
	    this.state = new IdleState<REQUEST>(configuration, request);
	    setState(new SendingRequestState<REQUEST>(configuration, request));
	}
	
	private void setState(TransactionState state) {
	    if (this.state != state) {
		log.debug(String.format("Req %-20s : TxId %-10s : %s -> %s", request, state.getTransactionId(), this.state, state));
		this.state = state;
		setState(this.state.activate());
	    }
	}
	
	public String getTransactionId() {
	    return state.getTransactionId();
	}
	
	@Override
	public void abort() {
	    setState(state.abort());
	}

	@Override
	public REQUEST getRequest() {
	    return request;
	}
    }
    

}
