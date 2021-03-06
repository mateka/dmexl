/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
        this(new NoOpOptimizer(), new TaskExecutor(nThreads));
    }
    
    public TaskExecutionContext(ITreeOptimizer treeOptimizer, IExecutor executor) {
        this(treeOptimizer, executor, new Random());
    }

    public TaskExecutionContext(ITreeOptimizer treeOptimizer, IExecutor executor, Random rnd) {
        super(treeOptimizer, rnd);
        this.executor = executor;
    }
    
    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public <T> Iterator<T> iterator(List<T> i) {
        return i.iterator();
    }
        
    @Override
    public <Result> Result execute(Algorithm<Result> algo) throws Exception {
        return optimize(algo).accept(this);
    }
    
    private IExecutor executor;
}
