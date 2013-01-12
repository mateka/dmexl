/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.compound.AccumulateTransformOperation;

/**
 *
 * @author matek
 */
public class AccumulateTransformOperationNode<R, R1, A>
        extends BinaryNode<
        AccumulateTransformOperation<R, R1, A>, Algorithm<IAccumulateOperation<R, R1>>, Algorithm<ITransformOperation<R1, A>>> {

    public AccumulateTransformOperationNode(Algorithm<IAccumulateOperation<R, R1>> left, Algorithm<ITransformOperation<R1, A>> right) {
        super(left, right);
    }

    @Override
    public AccumulateTransformOperation<R, R1, A> execute(IExecutionContext ctx) throws Exception {
        ITransformOperation<R1, A> top = ctx.getExecutor().execute(getRight(), ctx);
        IAccumulateOperation<R, R1> aop = ctx.getExecutor().execute(getLeft(), ctx);
        
        return new AccumulateTransformOperation<>(aop, top);
    }

    @Override
    public AccumulateTransformOperation<R, R1, A> accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }
}
