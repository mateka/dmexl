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
public interface ITreeOptimizer {
    public <T> IdentityNode<T> optimize(IdentityNode<T> node);
    public <T> SetNode<T> optimize(SetNode<T> node);
    public <T, F extends IFilterOperation<T>, C extends Collection<T>> Algorithm<C> optimize(FilterNode<T, F, C> node);
    public <R, E, O extends ITransformOperation<R, E>, C extends Collection<R>> TransformNode<R, E, O, C> optimize(TransformNode<R, E, O, C> node);
    public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> optimize(AccumulateNode<R, E, O> node);
    public <Result> Algorithm<Result> optimize(Algorithm<Result> node);
}
