/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author matek
 */
public class ResultType<Result> extends IResultType<Result> {

    public ResultType(Result result, boolean ok) {
        super(ok);
        this.result = result;
    }

    public ResultType(Result result) {
        super(true);
        this.result = result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return !isDone();
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public Result get() throws InterruptedException, ExecutionException {
        return result;
    }

    @Override
    public Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return get();
    }
    
    private Result result;
}
