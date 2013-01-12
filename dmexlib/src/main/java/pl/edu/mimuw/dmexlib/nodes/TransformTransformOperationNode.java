/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.compound.TransformTransformOperation;

/**
 *
 * @author matek
 */
public class TransformTransformOperationNode<R, R1, A>
        extends BinaryNode<
        TransformTransformOperation<R, R1, A>, Algorithm<ITransformOperation<R, R1>>, Algorithm<ITransformOperation<R1, A>>> {

    public TransformTransformOperationNode(Algorithm<ITransformOperation<R, R1>> outer, Algorithm<ITransformOperation<R1, A>> inner) {
        super(outer, inner);
    }

    @Override
    public TransformTransformOperation<R, R1, A> execute(IExecutionContext ctx) throws Exception {
        ITransformOperation<R, R1> out = ctx.getExecutor().execute(getLeft(), ctx);
        ITransformOperation<R1, A> inn = ctx.getExecutor().execute(getRight(), ctx);
        
        return new TransformTransformOperation<>(out, inn);
    }

    @Override
    public TransformTransformOperation<R, R1, A> accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }
    
}
