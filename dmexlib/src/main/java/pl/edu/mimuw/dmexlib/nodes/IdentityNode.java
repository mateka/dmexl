/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.utils.ResultType;

/**
 *
 * @author matek
 */
public class IdentityNode<Type> extends UnaryNode<Type, Type> {

    public IdentityNode(Type argument) {
        super(argument);
    }

    @Override
    public IResultType<Type> execute(IExecutionContext ctx) {
        return new ResultType<>(getArgument());
    }

    @Override
    public IResultType<Type> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }

}
