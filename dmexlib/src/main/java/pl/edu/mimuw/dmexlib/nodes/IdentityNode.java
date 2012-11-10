/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

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
    public Type execute(IExecutionContext ctx) {
        return getArgument();
    }

    @Override
    public Type accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }

}
