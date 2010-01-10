package witspirit.transactional.client.fsm;

import witspirit.transactional.client.TransactionStatus;


public class SuccessState<REQUEST> extends BaseState<REQUEST> {

    public SuccessState(BaseState<REQUEST> sourceState) {
	super(sourceState);
    }

    @Override
    public TransactionState abort() {
	// Requesting applicative reversal of a succesful transaction
	return new SendingReversalState<REQUEST>(this, 0);
    }

    @Override
    public TransactionState activate() {
	flagStatus(TransactionStatus.SUCCESS);
	return this;
    }

}
