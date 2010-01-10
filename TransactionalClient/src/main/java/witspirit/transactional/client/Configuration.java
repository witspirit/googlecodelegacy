package witspirit.transactional.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configuration<REQUEST> {
    private RequestHandler<REQUEST> requestHandler;
    private TransactionRecorder<REQUEST> transactionRecorder;
    private List<Integer> repeatIntervalsInSeconds = new ArrayList<Integer>();
    private List<Integer> reversalIntervalsInSeconds = new ArrayList<Integer>();

    public Configuration(RequestHandler<REQUEST> requestHandler, TransactionRecorder<REQUEST> transactionRecorder) {
	this(requestHandler, transactionRecorder, Arrays.asList(0, 0, 0), Arrays.asList(60, 5 * 60, 60 * 60, 2 * 60 * 60));
    }

    public Configuration(RequestHandler<REQUEST> requestHandler, TransactionRecorder<REQUEST> transactionRecorder, List<Integer> repeatIntervalsInSeconds, List<Integer> reversalIntervalsInSeconds) {
	if (requestHandler == null) {
	    throw new NullPointerException("RequestHandler is mandatory !");
	}
	this.requestHandler = requestHandler;
	if (transactionRecorder == null) {
	    throw new NullPointerException("TransactionRecorder is mandatory !");
	}
	this.transactionRecorder = transactionRecorder;
	if (repeatIntervalsInSeconds != null) {
	    this.repeatIntervalsInSeconds.addAll(repeatIntervalsInSeconds);
	}
	if (reversalIntervalsInSeconds != null) {
	    this.reversalIntervalsInSeconds.addAll(reversalIntervalsInSeconds);
	}
    }

    public Configuration<REQUEST> setRepeatIntervals(Integer... repeatIntervalsInSeconds) {
	this.repeatIntervalsInSeconds.clear();
	this.repeatIntervalsInSeconds.addAll(Arrays.asList(repeatIntervalsInSeconds));
	return this;
    }

    public Configuration<REQUEST> setReversalIntervals(Integer... reversalIntervalsInSeconds) {
	this.reversalIntervalsInSeconds.clear();
	this.reversalIntervalsInSeconds.addAll(Arrays.asList(reversalIntervalsInSeconds));
	return this;
    }

    public int getMaximumRepeatAttempts() {
	return repeatIntervalsInSeconds.size();
    }

    public int getMaximumReversalAttempts() {
	return reversalIntervalsInSeconds.size();
    }

    public RequestHandler<REQUEST> getRequestHandler() {
	return requestHandler;
    }
    
    public TransactionRecorder<REQUEST> getTransactionRecorder() {
	return transactionRecorder;
    }
}
