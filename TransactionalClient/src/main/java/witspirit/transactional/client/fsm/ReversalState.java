package witspirit.transactional.client.fsm;

import witspirit.transactional.client.Configuration;

public class ReversalState<REQUEST> implements TransactionState {

    public ReversalState(REQUEST request, Configuration<REQUEST> configuration, String transactionId, int i) {
	// TODO Auto-generated constructor stub
    }

    @Override
    public TransactionState abort() {
	// TODO Auto-generated method stub
	return this;
    }

    @Override
    public TransactionState activate() {
	// TODO Auto-generated method stub
	return this;
    }

}
