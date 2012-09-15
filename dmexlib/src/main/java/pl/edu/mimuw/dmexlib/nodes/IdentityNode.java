/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;

/**
 *
 * @author matek
 */
public class IdentityNode<Type> extends UnaryNode<Type, Type> {

    public IdentityNode(Type argument) {
        super(argument);
    }

    @Override
    public ResultType<Type> execute(IExecutionContext ctx) {
        return new ResultType<>(getArgument());
    }

    @Override
    public ResultType<Type> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }

}
