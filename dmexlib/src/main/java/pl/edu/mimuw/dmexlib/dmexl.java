/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.*;
import pl.edu.mimuw.dmexlib.nodes.*;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
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
    
    /// Helper function for transforming result into set
    static public <E> IdentityNode<Set<E>> set(E element) {
        Set<E> result = new HashSet<>();
        result.add(element);
        return I(result);
    }
    
    static public <E> SetNode<E> set(Iterable<E> elements) {
        return set(I(elements));
    }
    
    static public <E, F extends IFilterOperation<E>, CT extends Collection<E>> FilterToHashSetNode<E, F> set(FilterNode<E, F, CT> filteredElements) {
        return new FilterToHashSetNode<>(filteredElements.getLeft(), filteredElements.getRight());
    }
    
    static public <R, E, O extends ITransformOperation<R, E>, CT extends Collection<R>> TransformToHashSetNode<R, E, O> set(TransformNode<R, E, O, CT> transformedElements) {
        return new TransformToHashSetNode<>(transformedElements.getLeft(), transformedElements.getRight());
    }
    
    static public <E> SetNode<E> set(Algorithm elements) {
        return new SetNode<>(elements);
    }
    
    /// Helper methods for creating filter nodes
    static public <E, F extends IFilterOperation<E>> FilterNode<E, F, ? extends List<E>> filter(Iterable<E> elements, F f) {
        return filter(I(elements), I(f));
    }
    
    static public <E, F extends IFilterOperation<E>> FilterNode<E, F, ? extends List<E>> filter(Iterable<E> elements, Algorithm f) {
        return filter(I(elements), f);
    }
    
    static public <E, F extends IFilterOperation<E>> FilterNode<E, F, ? extends List<E>> filter(Algorithm elements, F f) {
        return filter(elements, I(f));
    }
    
    static public <E, F extends IFilterOperation<E>> FilterNode<E, F, ? extends List<E>> filter(Algorithm elements, Algorithm f) {
        return new FilterToArrayListNode<>(elements, f);
    }

    /// Helper methods for creating transform nodes
    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O, ? extends List<R>> transform(Iterable<E> elements, O op) {
        return transform(I(elements), I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O, ? extends List<R>> transform(Iterable<E> elements, Algorithm op) {
        return transform(I(elements), op);
    }
    
    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O, ? extends List<R>> transform(Algorithm elements, O op) {
        return transform(elements, I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O, ? extends List<R>> transform(Algorithm elements, Algorithm op) {
        return new TransformToArrayListNode<>(elements, op);
    }

    /// Helper methods for creating accumulate nodes
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, O op, R zero) {
        return accumulate(I(elements), I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, O op, Algorithm zero) {
        return accumulate(I(elements), I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, Algorithm op, R zero) {
        return accumulate(I(elements), op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Iterable<E> elements, Algorithm op, Algorithm zero) {
        return accumulate(I(elements), op, zero);
    }
       
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, O op, R zero) {
        return accumulate(elements, I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, O op, Algorithm zero) {
        return accumulate(elements, I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, Algorithm op, R zero) {
        return accumulate(elements, op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm elements, Algorithm op, Algorithm zero) {
        return new AccumulateNode<>(elements, op, zero);
    }
}
