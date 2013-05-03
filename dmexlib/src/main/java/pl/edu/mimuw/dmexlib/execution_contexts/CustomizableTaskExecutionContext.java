/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.executors.CustomizableTaskExecutor;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;

/**
 *
 * @author matek
 */
public class CustomizableTaskExecutionContext extends OptimizingExecutionContext {

    public CustomizableTaskExecutionContext(CustomizableTaskExecutor executor, ITreeOptimizer treeOptimizer) {
        super(treeOptimizer);
        this.executor = executor;
    }

    @Override
    public <Result> Result execute(Algorithm<Result> algo) throws Exception {
        return optimize(algo).accept(this);
    }

    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public <T> Iterator<T> iterator(List<T> i) {
        return i.iterator();
    }
    
    private CustomizableTaskExecutor executor;
}
