/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import pl.edu.mimuw.dmexlib.IResultType;

/**
 *
 * @author matek
 */
public class TreeFutureResultType<T> extends FutureResultType<T> {

    public TreeFutureResultType(Future<IResultType<T>> task, IResultType<T> left, IResultType<T> right) {
        super(task);
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        left.cancel(mayInterruptIfRunning);
        right.cancel(mayInterruptIfRunning);
        return super.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return left.isCancelled() && right.isCancelled() && super.isCancelled();
    }

    @Override
    public boolean isDone() {
        return left.isDone() && right.isDone() && super.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return super.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(timeout, unit);
    }
    
    private IResultType<T> left;
    private IResultType<T> right;
}
