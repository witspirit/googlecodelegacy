package witspirit.transactional.client.fsm;

public interface TransactionState {
    String getTransactionId();
    TransactionState activate();
    TransactionState abort();
}
