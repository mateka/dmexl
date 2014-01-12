/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Random;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;

/**
 *
 * @author matek
 */
public abstract class OptimizingExecutionContext implements IExecutionContext {

    public OptimizingExecutionContext(ITreeOptimizer treeOptimizer) {
        this(treeOptimizer, new Random());
    }

    public OptimizingExecutionContext(ITreeOptimizer treeOptimizer, Random rnd) {
        this.optimizer = treeOptimizer;
        this.rnd = rnd;
    }

    public ITreeOptimizer getOptimizer() {
        return optimizer;
    }

    @Override
    public Random getRandom() {
        return rnd;
    }

    protected <Result> Algorithm<Result> optimize(Algorithm<Result> algo) {
        if (algo instanceof IdentityNode) {
            return getOptimizer().optimize((IdentityNode) algo);
        } else if (algo instanceof FilterNode) {
            return getOptimizer().optimize((FilterNode) algo);
        } else if (algo instanceof TransformNode) {
            return getOptimizer().optimize((TransformNode) algo);
        } else if (algo instanceof AccumulateNode) {
            return getOptimizer().optimize((AccumulateNode) algo);
        } else {
            return getOptimizer().optimize(algo);
        }
    }
    private final ITreeOptimizer optimizer;
    private final Random rnd;
}
