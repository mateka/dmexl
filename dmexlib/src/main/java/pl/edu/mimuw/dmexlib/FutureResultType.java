/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author matek
 */
public class FutureResultType<Result> implements IResultType<Result> {

    public FutureResultType(Future<ResultType<Result>> task) {
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
            ResultType<Result> res = getTask().get();
            return res.isOk();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    @Override
    public Result get() throws InterruptedException, ExecutionException {
        ResultType<Result> res = getTask().get();
        return res.get();
    }

    @Override
    public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        ResultType<Result> res = getTask().get(timeout, unit);
        return res.get();
    }

    protected Future<ResultType<Result>> getTask() {
        return task;
    }
    private Future<ResultType<Result>> task;
}
