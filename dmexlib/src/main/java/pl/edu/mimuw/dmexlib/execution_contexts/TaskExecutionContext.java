/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.TaskExecutor;
import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;
import pl.edu.mimuw.dmexlib.optimizers.NoOpOptimizer;

/**
 *
 * @author matek
 */
public class TaskExecutionContext extends OptimizingExecutionContext {

    public TaskExecutionContext(int nThreads) {
        this(new NoOpOptimizer(), nThreads);
    }
    
    public TaskExecutionContext(ITreeOptimizer treeOptimizer, int nThreads) {
        super(treeOptimizer);
        this.executor = new TaskExecutor(nThreads);
    }
    @Override
    public <Result> Result execute(Algorithm<Result> algo) throws InterruptedException, ExecutionException {
        return getOptimizer().optimize(algo).accept(this);
    }

    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public <T> Iterator<T> iterator(List<T> i) {
        return i.iterator();
    }
    
    private TaskExecutor executor;
}
