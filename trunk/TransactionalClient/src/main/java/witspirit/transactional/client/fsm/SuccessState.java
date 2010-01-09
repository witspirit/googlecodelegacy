package witspirit.transactional.client.fsm;

import witspirit.transactional.client.Configuration;

public class SuccessState<REQUEST> implements TransactionState {

    public SuccessState(REQUEST request, Configuration<REQUEST> configuration, String transactionId) {
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
