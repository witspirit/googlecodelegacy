package witspirit.transactional.client;

import java.util.concurrent.TimeoutException;

public interface RequestHandler<REQUEST> {
    String doSendRequest(REQUEST request, boolean isRepeat) throws TimeoutException; // Returns transactions id
    void doSendReversal(REQUEST request, String transactionId) throws TimeoutException;
}
