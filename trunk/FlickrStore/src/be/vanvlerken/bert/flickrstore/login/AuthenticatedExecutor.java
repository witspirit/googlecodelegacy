/**
 * @author wItspirit
 * 5-nov-2005
 * DynamicExecutor.java
 */

package be.vanvlerken.bert.flickrstore.login;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class AuthenticatedExecutor implements ExecutorService
{
    private ExecutorService            executor;
    private AuthenticatedThreadFactory authenticatedThreadFactory;

    public AuthenticatedExecutor(SyncLoginManager loginManager, AuthorizationHandler authHandler)
    {
        authenticatedThreadFactory = new AuthenticatedThreadFactory(loginManager, authHandler);
        executor = Executors.newFixedThreadPool(2,authenticatedThreadFactory);
    }
    
    public void resetExecutor()
    {
        executor.shutdown();
        executor = Executors.newFixedThreadPool(2,authenticatedThreadFactory);
    }

    public boolean awaitTermination(long arg0, TimeUnit arg1) throws InterruptedException
    {
        return executor.awaitTermination(arg0, arg1);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> arg0) throws InterruptedException
    {
        return executor.invokeAll(arg0);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2) throws InterruptedException
    {
        return executor.invokeAll(arg0, arg1, arg2);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> arg0) throws InterruptedException, ExecutionException
    {
        return executor.invokeAny(arg0);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> arg0, long arg1, TimeUnit arg2) throws InterruptedException, ExecutionException, TimeoutException
    {
        return executor.invokeAny(arg0, arg1, arg2);
    }

    public boolean isShutdown()
    {
        return executor.isShutdown();
    }

    public boolean isTerminated()
    {
        return executor.isTerminated();
    }

    public void shutdown()
    {
        executor.shutdown();
    }

    public List<Runnable> shutdownNow()
    {
        return executor.shutdownNow();
    }

    public Future< ? > submit(Runnable arg0)
    {
        return executor.submit(arg0);
    }

    public <T> Future<T> submit(Runnable arg0, T arg1)
    {
        return executor.submit(arg0, arg1);
    }

    public <T> Future<T> submit(Callable<T> arg0)
    {
        return executor.submit(arg0);
    }

    public void execute(Runnable arg0)
    {
        executor.execute(arg0);
    }

}
