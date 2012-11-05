/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author matek
 */
public class TreeFuture<T> implements Future<T> {

    public TreeFuture(Future<T> task, Future<T> left, Future<T> right) {
        this.node = task;
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        left.cancel(mayInterruptIfRunning);
        right.cancel(mayInterruptIfRunning);
        return node.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return left.isCancelled() && right.isCancelled() && node.isCancelled();
    }

    @Override
    public boolean isDone() {
        return left.isDone() && right.isDone() && node.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return node.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return node.get(timeout, unit);
    }
    
    private Future<T> node;
    private Future<T> left;
    private Future<T> right;
}
