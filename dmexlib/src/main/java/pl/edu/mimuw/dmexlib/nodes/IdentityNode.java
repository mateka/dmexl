/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

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
    public ResultType<Type> sequentialExecute(IExecutionContext ctx) {
        return new ResultType<>(getArgument());
    }

    @Override
    public ResultType<Type> multiCPUExecute(IExecutionContext ctx) {
        return sequentialExecute(ctx);
    }

    @Override
    public ResultType<Type> GPUExecute(IExecutionContext ctx) {
        return sequentialExecute(ctx);
    }
    
}
