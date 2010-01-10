package witspirit.transactional.client.fsm;

import java.util.concurrent.TimeoutException;

import witspirit.transactional.client.Configuration;
import witspirit.transactional.client.TransactionStatus;

public class BaseState<REQUEST> implements TransactionState {
    private REQUEST request;
    private Configuration<REQUEST> configuration;
    private String transactionId;
    
    protected BaseState(Configuration<REQUEST> configuration, REQUEST request) {
	this.configuration = configuration;
	this.request = request;
    }
    
    protected BaseState(BaseState<REQUEST> sourceState) {
	this.request = sourceState.request;
	this.configuration = sourceState.configuration;
	this.transactionId = sourceState.transactionId;
    }
    
    @Override
    public TransactionState abort() {
	throw new UnsupportedOperationException("This method is not allowed for this state");
    }
    
    @Override
    public TransactionState activate() {
	throw new UnsupportedOperationException("This method is not allowed for this state");
    }
    
    public REQUEST getRequest() {
	return request;
    }
    
    public String getTransactionId() {
	return transactionId;
    }
    
    protected int getMaximumRepeatAttempts() {
	return configuration.getMaximumRepeatAttempts();
    }
    
    protected int getMaximumReversalAttempts() {
	return configuration.getMaximumReversalAttempts();
    }
    
    protected void sendRequest(boolean isRepeat) throws TimeoutException {
	transactionId = configuration.getRequestHandler().doSendRequest(request, isRepeat);
    }
    
    protected void sendReversal() throws TimeoutException {
	configuration.getRequestHandler().doSendReversal(request, transactionId);
    }
    
    protected void flagStatus(TransactionStatus status) {
	configuration.getRequestHandler().transactionDone(request, transactionId, status);
    }
    
    @Override
    public String toString() {
	String simpleName = this.getClass().getSimpleName();
	String stateName = simpleName.substring(0, simpleName.length()-5);
        return String.format("%-20s", stateName);
    }
    
}
