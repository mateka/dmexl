/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.List;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 * Interface for all executors.
 * @author matek
 */
public interface IExecutor {
    public <T> IResultType<T> execute(IdentityNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException;
    public <T, F extends IFilterOperation<T>> IResultType<List<T>> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException;
    public <R, E, O extends ITransformOperation<R, E>> IResultType<List<R>> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException;
    public <R, E, O extends IAccumulateOperation<R, E>> IResultType<R> execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException;    
    public <Result> IResultType<Result> execute(Algorithm<Result> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException;
    
    public void shutdown();
}
