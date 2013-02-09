/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.*;
import pl.edu.mimuw.dmexlib.nodes.*;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;
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
//    static public <E> IdentityNode<Set<E>> set(E element) {
//        Set<E> result = new HashSet<>();
//        result.add(element);
//        return I(result);
//    }
//        
//    /// Helper methods for creating filter nodes
//    static public <E, F extends IFilterOperation<E>> FilterNode<E, F> filter(List<E> elements, F f) {
//        return filter(I(elements), I(f));
//    }
//    
//    static public <E, F extends IFilterOperation<E>> FilterNode<E, F> filter(List<E> elements, Algorithm f) {
//        return filter(I(elements), f);
//    }
//    
//    static public <E, F extends IFilterOperation<E>> FilterNode<E, F> filter(Algorithm<List<E>> elements, F f) {
//        return filter(elements, I(f));
//    }
//    
//    static public <E, F extends IFilterOperation<E>> FilterNode<E, F> filter(Algorithm<List<E>> elements, Algorithm f) {
//        return new FilterNode<>(elements, f);
//    }
    
    /// Helper methods for creating generator nodes
    static public <E, O extends IGenerateOperation<E>> GenerateNode<E,O> generateN(int count, O op) {
        return generateN(I(count), I(op));
    }
    
    static public <E, O extends IGenerateOperation<E>> GenerateNode<E,O> generateN(int count, Algorithm<O> op) {
        return generateN(I(count), op);
    }
    
    static public <E, O extends IGenerateOperation<E>> GenerateNode<E,O> generateN(Algorithm<Integer> count, O op) {
        return generateN(count, I(op));
    }
    
    static public <E, O extends IGenerateOperation<E>> GenerateNode<E,O> generateN(Algorithm<Integer> count, Algorithm<O> op) {
        return new GenerateNode<>(count, op);
    }

    /// Helper methods for creating transform nodes
    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(List<E> elements, O op) {
        return transform(I(elements), I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(List<E> elements, Algorithm op) {
        return transform(I(elements), op);
    }
    
    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Algorithm<List<E>> elements, O op) {
        return transform(elements, I(op));
    }

    static public <R, E, O extends ITransformOperation<R, E>> TransformNode<R, E, O> transform(Algorithm<List<E>> elements, Algorithm op) {
        return new TransformNode<>(elements, op);
    }

    /// Helper methods for creating accumulate nodes
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, O op, R zero) {
        return accumulate(I(elements), I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, O op, Algorithm zero) {
        return accumulate(I(elements), I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, Algorithm op, R zero) {
        return accumulate(I(elements), op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, Algorithm op, Algorithm zero) {
        return accumulate(I(elements), op, zero);
    }
       
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, O op, R zero) {
        return accumulate(elements, I(op), I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, O op, Algorithm zero) {
        return accumulate(elements, I(op), zero);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, Algorithm op, R zero) {
        return accumulate(elements, op, I(zero));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, Algorithm op, Algorithm zero) {
        return new AccumulateNode<>(elements, op, zero);
    }
}
