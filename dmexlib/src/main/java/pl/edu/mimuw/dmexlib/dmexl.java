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
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;

/**
 * Class for utility functions for creating algorithms/expressions
 *
 * @author matek
 */
public abstract class dmexl {

    /// Helper function for returning identity node
    static public <T> IdentityNode<T> I(T t) {
        return new IdentityNode<T>(t);
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
    static public <E, O extends IGenerateOperation<E>> GenerateNode<E, O> generateN(int count, O op) {
        return generateN(I(count), I(op));
    }

    static public <E, O extends IGenerateOperation<E>> GenerateNode<E, O> generateN(int count, Algorithm<O> op) {
        return generateN(I(count), op);
    }

    static public <E, O extends IGenerateOperation<E>> GenerateNode<E, O> generateN(Algorithm<Integer> count, O op) {
        return generateN(count, I(op));
    }

    static public <E, O extends IGenerateOperation<E>> GenerateNode<E, O> generateN(Algorithm<Integer> count, Algorithm<O> op) {
        return new GenerateNode<E, O>(count, op);
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
        return new TransformNode<R, E, O>(elements, op);
    }

    /// Helper methods for creating accumulate nodes
    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, O op) {
        return accumulate(I(elements), I(op));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(List<E> elements, Algorithm op) {
        return accumulate(I(elements), op);
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, O op) {
        return accumulate(elements, I(op));
    }

    static public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode<R, E, O> accumulate(Algorithm<List<E>> elements, Algorithm op) {
        return new AccumulateNode<R, E, O>(elements, op);
    }

    /// Helper methods for creating pso nodes
    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(List<Particle> parts, Cost cost, Convergence cov) {
        return pso(I(parts), I(cost), I(cov));
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(List<Particle> parts, Cost cost, Algorithm<Convergence> cov) {
        return pso(I(parts), I(cost), cov);
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(List<Particle> parts, Algorithm<Cost> cost, Convergence cov) {
        return pso(I(parts), cost, I(cov));
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(List<Particle> parts, Algorithm<Cost> cost, Algorithm<Convergence> cov) {
        return pso(I(parts), cost, cov);
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(Algorithm<List<Particle>> parts, Cost cost, Convergence cov) {
        return pso(parts, I(cost), I(cov));
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(Algorithm<List<Particle>> parts, Cost cost, Algorithm<Convergence> cov) {
        return pso(parts, I(cost), cov);
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(Algorithm<List<Particle>> parts, Algorithm<Cost> cost, Convergence cov) {
        return pso(parts, cost, I(cov));
    }

    static public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> PSONode<VT, P, Particle, Cost, Convergence> pso(Algorithm<List<Particle>> parts, Algorithm<Cost> cost, Algorithm<Convergence> cov) {
        return new PSONode<VT, P, Particle, Cost, Convergence>(parts, cost, cov);
    }
}
