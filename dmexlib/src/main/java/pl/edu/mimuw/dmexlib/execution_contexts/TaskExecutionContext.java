/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.TaskExecutor;
import pl.edu.mimuw.dmexlib.optimizers.NoOpOptimizer;

/**
 *
 * @author matek
 */
public class TaskExecutionContext implements IExecutionContext {

    public TaskExecutionContext() {
        this.executor = new TaskExecutor(new NoOpOptimizer());
    }
    @Override
    public <Result> ResultType<Result> execute(Algorithm<Result> algo) throws InterruptedException, ExecutionException {
        return algo.accept(this);
    }

    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public <T> Iterator<T> iterator(Iterable<T> i) {
        return i.iterator();
    }
    
    private TaskExecutor executor;
}
