/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.GenerateNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.PSONode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;

/**
 * Interface for all executors.
 * @author matek
 */
public interface IExecutor {
    public <T> T execute(IdentityNode<T> algo, IExecutionContext ctx) throws Exception;
    public <T, F extends IFilterOperation<T>> List<T> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws Exception;
    public <E, O extends IGenerateOperation<E>> List<E> execute(GenerateNode<E, O> algo, IExecutionContext ctx) throws Exception;
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws Exception;
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws Exception;
    public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> Particle execute(PSONode<VT, P, Particle, Cost, Convergence> algo, IExecutionContext ctx) throws Exception;
    public <Result> Result execute(Algorithm<Result> algo, IExecutionContext ctx) throws Exception;
    
    public void shutdown();
}
