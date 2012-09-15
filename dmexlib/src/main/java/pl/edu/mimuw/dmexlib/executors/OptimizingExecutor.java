/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;

/**
 *
 * @author matek
 */
public abstract class OptimizingExecutor implements IExecutor {

    public OptimizingExecutor(ITreeOptimizer treeOptimizer) {
        this.optimizer = treeOptimizer;
    }

    public ITreeOptimizer getOptimizer() {
        return optimizer;
    }
    private ITreeOptimizer optimizer;
}
