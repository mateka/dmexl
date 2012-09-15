/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.SetNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 * Simple, sequential executor
 *
 * @author matek
 */
public class SequentialExecutor implements IExecutor {

    @Override
    public <Result> ResultType<Result> execute(Algorithm<Result> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return algo.execute(ctx);
    }

    @Override
    public <T> ResultType<T> execute(IdentityNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<T>)algo, ctx);
    }

    @Override
    public <T> ResultType<Set<T>> execute(SetNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<Set<T>>)algo, ctx);
    }

    @Override
    public <T, F extends IFilterOperation<T>, C extends Collection<T>> ResultType<C> execute(FilterNode<T, F, C> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<C>)algo, ctx);
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>, C extends Collection<R>> ResultType<C> execute(TransformNode<R, E, O, C> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<C>)algo, ctx);
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> ResultType<R> execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<R>)algo, ctx);
    }
}
