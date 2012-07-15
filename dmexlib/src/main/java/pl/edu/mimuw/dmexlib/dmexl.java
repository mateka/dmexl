/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.TransformToArrayListNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 * Class for utility functions for creating algorithms/expressions
 *
 * @author matek
 */
public abstract class dmexl {

    /// Helper function for returning identity node
    static public <T> IdentityNode<T> I(T t) {
        return new IdentityNode<>(t);
    }

    static public <T> Algorithm<T> I(Algorithm<T> t) {
        return t;
    }

    /// Helper methods for creating transform nodes
    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Iterable<E> elements, O op) {
        return transform(I(elements), I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Iterable<E> elements, Algorithm<O> op) {
        return transform(I(elements), op);
    }
    
    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Algorithm elements, O op) {
        return transform(elements, I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Algorithm elements, Algorithm<O> op) {
        return new TransformToArrayListNode<>(elements, op);
    }

    /// Helper methods for creating accumulate nodes
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, O op, R zero) {
        return accumulate(I(elements), I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, O op, Algorithm<R> zero) {
        return accumulate(I(elements), I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, Algorithm<O> op, R zero) {
        return accumulate(I(elements), op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, Algorithm<O> op, Algorithm<R> zero) {
        return accumulate(I(elements), op, zero);
    }
       
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, O op, R zero) {
        return accumulate(elements, I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, O op, Algorithm<R> zero) {
        return accumulate(elements, I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, Algorithm<O> op, R zero) {
        return accumulate(elements, op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, Algorithm<O> op, Algorithm<R> zero) {
        return new AccumulateNode<>(elements, op, zero);
    }
}
