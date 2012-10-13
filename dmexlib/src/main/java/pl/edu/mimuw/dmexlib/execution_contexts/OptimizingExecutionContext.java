/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;

/**
 *
 * @author matek
 */
public abstract class OptimizingExecutionContext implements IExecutionContext {
    
    public OptimizingExecutionContext(ITreeOptimizer treeOptimizer) {
        this.optimizer = treeOptimizer;
    }

    public ITreeOptimizer getOptimizer() {
        return optimizer;
    }
    private ITreeOptimizer optimizer;
}
