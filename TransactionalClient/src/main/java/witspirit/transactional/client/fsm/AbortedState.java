package witspirit.transactional.client.fsm;

import witspirit.transactional.client.Configuration;

public class AbortedState<REQUEST> implements TransactionState {

    public AbortedState(REQUEST request, Configuration<REQUEST> configuration, Object object) {
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
