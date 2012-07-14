/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.List;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 * Class for utility functions for creating algorithms/expressions
 *
 * @author matek
 */
public class dmexl {
    /// Factory methods for creating nodes for transform algorithm

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(List<E> elements, O op) {
        return new TransformNode<>(I(elements), I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(List<E> elements, Algorithm<O> op) {
        return new TransformNode<>(I(elements), op);
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Algorithm<List<E>> elements, O op) {
        return new TransformNode<>(elements, I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Algorithm<List<E>> elements, Algorithm<O> op) {
        return new TransformNode<>(elements, op);
    }

    /// Factory methods for creating nodes for accumulate algorithm
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, O op, R zero) {
        return new AccumulateNode<>(I(elements), I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, O op, Algorithm<R> zero) {
        return new AccumulateNode<>(I(elements), I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, Algorithm<O> op, R zero) {
        return new AccumulateNode<>(I(elements), op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, Algorithm<O> op, Algorithm<R> zero) {
        return new AccumulateNode<>(I(elements), op, zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, O op, R zero) {
        return new AccumulateNode<>(elements, I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, O op, Algorithm<R> zero) {
        return new AccumulateNode<>(elements, I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, Algorithm<O> op, R zero) {
        return new AccumulateNode<>(elements, op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, Algorithm<O> op, Algorithm<R> zero) {
        return new AccumulateNode<>(elements, op, zero);
    }

    /// Helper function for returning identity node
    static public <T> IdentityNode<T> I(T t) {
        return new IdentityNode<>(t);
    }
}
