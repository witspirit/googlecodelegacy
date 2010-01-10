package witspirit.transactional.client.fsm;

import witspirit.transactional.client.TransactionStatus;


public class InterventionRequiredState<REQUEST> extends BaseState<REQUEST> {

    public InterventionRequiredState(BaseState<REQUEST> sourceState) {
	super(sourceState);
    }

    @Override
    public TransactionState abort() {
	// Do nothing, this is an end state that matches the abort trigger
	return this;
    }

    @Override
    public TransactionState activate() {
	flagStatus(TransactionStatus.INTERVENTION_REQUIRED);
	return this;
    }

}
