package witspirit.transactional.client;

public interface Transaction<REQUEST> {
    String getTransactionId();
    REQUEST getRequest();
    void abort();
}
