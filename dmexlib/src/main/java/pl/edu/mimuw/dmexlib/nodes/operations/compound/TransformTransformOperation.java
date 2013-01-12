/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations.compound;

import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TransformTransformOperation<Result, TResult, Argument> implements ITransformOperation<Result, Argument> {

    public TransformTransformOperation(ITransformOperation<Result, TResult> outer, ITransformOperation<TResult, Argument> inner) {
        this.inner = inner;
        this.outer = outer;
    }

    @Override
    public Result invoke(Argument arg) {
        return outer.invoke(inner.invoke(arg));
    }
    
    private ITransformOperation<TResult, Argument> inner;
    private ITransformOperation<Result, TResult> outer;
}
