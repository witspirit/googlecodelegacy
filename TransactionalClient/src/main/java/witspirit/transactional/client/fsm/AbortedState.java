package witspirit.transactional.client.fsm;

import witspirit.transactional.client.TransactionStatus;


public class AbortedState<REQUEST> extends BaseState<REQUEST> {

    public AbortedState(BaseState<REQUEST> sourceState) {
	super(sourceState);
    }

    @Override
    public TransactionState abort() {
	// Do nothing, this is an end state that matches the abort trigger
	return this;
    }

    @Override
    public TransactionState activate() {
	flagStatus(TransactionStatus.FAILURE);
	return this;
    }

}
