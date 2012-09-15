/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.optimizers;

import java.util.Collection;
import java.util.Set;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.SetNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class NoOpOptimizer implements ITreeOptimizer {

    @Override
    public <T> Algorithm<T> optimize(IdentityNode<T> node) {
        return node;
    }

    @Override
    public <T> Algorithm<Set<T>> optimize(SetNode<T> node) {
        return node;
    }

    @Override
    public <T, F extends IFilterOperation<T>, C extends Collection<T>> Algorithm<C> optimize(FilterNode<T, F, C> node) {
        return node;
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>, C extends Collection<R>> Algorithm<C> optimize(TransformNode<R, E, O, C> node) {
        return node;
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> Algorithm<R> optimize(AccumulateNode<R, E, O> node) {
        return node;
    }

    @Override
    public <Result> Algorithm<Result> optimize(Algorithm<Result> node) {
        return node;
    }
    
}
