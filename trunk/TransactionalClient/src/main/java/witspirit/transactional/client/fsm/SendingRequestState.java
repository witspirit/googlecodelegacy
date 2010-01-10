package witspirit.transactional.client.fsm;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import witspirit.transactional.client.Configuration;

public class SendingRequestState<REQUEST> extends BaseState<REQUEST> {
    private AtomicBoolean abort = new AtomicBoolean(false);
    private int attemptNr = 0;

    public SendingRequestState(Configuration<REQUEST> configuration, REQUEST request) {
	super(configuration, request);
    }
    
    public SendingRequestState(BaseState<REQUEST> sourceState, int attemptNr) {
	super(sourceState);
	this.attemptNr = attemptNr;
    }
    
    public TransactionState activate() {
	try {
	    // TODO Take into account delay (if any) !
	    sendRequest(attemptNr != 0);
	    if (abort.get()) {
		return new SendingReversalState<REQUEST>(this, 0);
	    } else {
		return new SuccessState<REQUEST>(this);
	    }
	} catch (TimeoutException e) {
	    if (abort.get()) {
		return new SendingReversalState<REQUEST>(this, 0);
	    } else {
		if (attemptNr < getMaximumRepeatAttempts()) {
		    return new SendingRequestState<REQUEST>(this, attemptNr+1);
		} else {
		    return new SendingReversalState<REQUEST>(this, 0);
		}
	    }
	}
    }

    @Override
    public TransactionState abort() {
	abort.set(true);
	return this;
    }

}
