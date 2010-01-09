package witspirit.transactional.client.fsm;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import witspirit.transactional.client.Configuration;

public class SendingRequestState<REQUEST> implements TransactionState {
    private REQUEST request;
    private Configuration<REQUEST> configuration;
    private String transactionId;
    private AtomicBoolean abort = new AtomicBoolean(false);

    public SendingRequestState(REQUEST request, Configuration<REQUEST> configuration) {
	this.request = request;
	this.configuration = configuration;
    }
    
    public TransactionState activate() {
	try {
	    transactionId = configuration.getRequestHandler().doSendRequest(request, false);
	    // configuration.getRequestHandler().transactionDone(request, transactionId, TransactionStatus.SUCCESS);
	    if (abort.get()) {
		return new ReversalState<REQUEST>(request, configuration, transactionId, 0);
	    } else {
		return new SuccessState<REQUEST>(request, configuration, transactionId);
	    }
	} catch (TimeoutException e) {
	    if (abort.get()) {
		return new AbortedState<REQUEST>(request, configuration, null);
	    } else {
		return new ReversalState<REQUEST>(request, configuration, null, 0);
	    }
	    // configuration.getRequestHandler().transactionDone(request, transactionId, TransactionStatus.FAILURE);
	}
    }

    @Override
    public TransactionState abort() {
	abort.set(true);
	return this;
    }

}
