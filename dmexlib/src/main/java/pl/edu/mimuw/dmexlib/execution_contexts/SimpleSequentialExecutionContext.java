/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.SkipIterator;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.SequentialExecutor;
import pl.edu.mimuw.dmexlib.optimizers.NoOpOptimizer;

/**
 *
 * @author matek
 */
public class SimpleSequentialExecutionContext implements IExecutionContext {

    public SimpleSequentialExecutionContext() {
        this.executor = new SequentialExecutor(new NoOpOptimizer());
    }

    public SimpleSequentialExecutionContext(SequentialExecutor executor, int start, int skip) {
        this.executor = executor;
        this.start = start;
        this.skip = skip;
    }

    @Override
    public <T> Iterator<T> iterator(Iterable<T> coll) {
        if (null != skip) {
            return new SkipIterator(coll, this.start, this.skip);
        } else {
            return coll.iterator();
        }
    }

    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public <Result> ResultType<Result> execute(Algorithm<Result> algo) throws InterruptedException, ExecutionException {
        return getExecutor().execute(algo, this);
    }
    private SequentialExecutor executor;
    private Integer start;
    private Integer skip;
}
