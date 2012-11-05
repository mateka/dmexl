/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.List;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
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
    public <Result> Result execute(Algorithm<Result> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return algo.execute(ctx);
    }

    @Override
    public <T> T execute(IdentityNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<T>)algo, ctx);
    }

    @Override
    public <T, F extends IFilterOperation<T>> List<T> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<List<T>>)algo, ctx);
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<List<R>>)algo, ctx);
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return execute((Algorithm<R>)algo, ctx);
    }

    @Override
    public void shutdown() {}
    
}
