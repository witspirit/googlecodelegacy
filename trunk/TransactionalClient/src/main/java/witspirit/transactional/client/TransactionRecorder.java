package witspirit.transactional.client;


public interface TransactionRecorder<REQUEST> {
    void transactionDone(REQUEST request, String transactionId, TransactionStatus status);
}
