package witspirit.transactional.client.fsm;

public interface TransactionState {
    TransactionState activate();
    TransactionState abort();
}
