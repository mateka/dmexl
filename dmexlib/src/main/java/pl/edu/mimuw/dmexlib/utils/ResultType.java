/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import pl.edu.mimuw.dmexlib.IResultType;

/**
 *
 * @author matek
 */
public class ResultType<Result> implements IResultType<Result> {

    public ResultType(Result result, boolean ok) {
        this.result = result;
        this.ok = ok;
    }

    public ResultType(Result result) {
        this.result = result;
        this.ok = true;
    }
    
    @Override
    public boolean isOk() {
        return ok;
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
    private boolean ok;
}
