/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.SequentialExecutor;
import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;
import pl.edu.mimuw.dmexlib.optimizers.NoOpOptimizer;

/**
 *
 * @author matek
 */
public class SimpleSequentialExecutionContext extends OptimizingExecutionContext {

    public SimpleSequentialExecutionContext() {
        this(new NoOpOptimizer());
    }

    public SimpleSequentialExecutionContext(ITreeOptimizer treeOptimizer) {
        super(treeOptimizer);
        this.executor = new SequentialExecutor();
    }

    @Override
    public <T> Iterator<T> iterator(List<T> coll) {
        return coll.iterator();
    }

    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public <Result> IResultType<Result> execute(Algorithm<Result> algo) throws InterruptedException, ExecutionException {
        return getOptimizer().optimize(algo).accept(this);
    }
    private SequentialExecutor executor;
}
