/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations.compound;

import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class AccumulateTransformOperation<Result, TResult, Argument> implements IAccumulateOperation<Result, Argument> {

    public AccumulateTransformOperation(IAccumulateOperation<Result, TResult> accumulateOp, ITransformOperation<TResult, Argument> transformOp) {
        this.accumulateOp = accumulateOp;
        this.transformOp = transformOp;
    }

    @Override
    public Result invoke(Argument arg) {
        return accumulateOp.invoke(transformOp.invoke(arg));
    }

    @Override
    public Result invoke(Result left, Result right) {
        return accumulateOp.invoke(left, right);
    }
    
    private IAccumulateOperation<Result, TResult> accumulateOp;
    private ITransformOperation<TResult, Argument> transformOp;
}
