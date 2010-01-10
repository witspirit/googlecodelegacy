package witspirit.transactional.client.fsm;

import java.util.concurrent.TimeoutException;

public class SendingReversalState<REQUEST> extends BaseState<REQUEST> {
    private int attemptNr;

    public SendingReversalState(BaseState<REQUEST> sourceState, int attemptNr) {
	super(sourceState);
	this.attemptNr = attemptNr;
    }

    @Override
    public TransactionState abort() {
	// Do nothing, we are already reversing !
	return this;
    }

    @Override
    public TransactionState activate() {
	try {
	    // TODO Take into account delay !
	    sendReversal();
	    return new AbortedState<REQUEST>(this);
	} catch (TimeoutException e) {
	    if (attemptNr < getMaximumReversalAttempts()) {
		return new SendingReversalState<REQUEST>(this, attemptNr+1);
	    } else {
		return new InterventionRequiredState<REQUEST>(this);
	    }
	}
    }

}
