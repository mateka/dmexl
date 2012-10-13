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
public class FutureResultType<Result> implements IResultType<Result> {

    public FutureResultType(Future<IResultType<Result>> task) {
        this.task = task;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return getTask().cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return getTask().isCancelled();
    }

    @Override
    public boolean isDone() {
        return getTask().isDone();
    }

    @Override
    public boolean isOk() {
        try {
            IResultType<Result> res = getTask().get();
            return res.isOk();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    @Override
    public Result get() throws InterruptedException, ExecutionException {
        IResultType<Result> res = getTask().get();
        return res.get();
    }

    @Override
    public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        IResultType<Result> res = getTask().get(timeout, unit);
        return res.get();
    }

    protected Future<IResultType<Result>> getTask() {
        return task;
    }
    private Future<IResultType<Result>> task;
}
