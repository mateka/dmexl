/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;

/**
 *
 * @author matek
 */
public class SetNode<Type> extends UnaryNode<Set<Type>, Algorithm<Iterable<Type>>> {

    public SetNode(Algorithm<Iterable<Type>> argument) {
        super(argument);
    }

    @Override
    public ResultType<Set<Type>> execute(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        ResultType<Iterable<Type>> arg = ctx.getExecutor().execute(getArgument(), ctx);
        if (!arg.isOk()) {
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<Type> elements = ctx.iterator(arg.get());

        // Do sequential algorithm
        Set<Type> resultSet = new HashSet<>();
        while (elements.hasNext()) {
            resultSet.add(elements.next());
        }
        return new ResultType<>(resultSet);
    }

    @Override
    public ResultType<Set<Type>> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }

}
