/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.optimizers;

import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.AccumulateTransformOperationNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.TransformTransformOperationNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.compound.AccumulateTransformOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.compound.TransformTransformOperation;

/**
 *
 * @author matek
 */
public class SimpleOptimizer implements ITreeOptimizer {

    @Override
    public <T> Algorithm<T> optimize(IdentityNode<T> node) {
        return node;
    }

    @Override
    public <T, F extends IFilterOperation<T>> Algorithm<List<T>> optimize(FilterNode<T, F> node) {
        return node;
    }

    @Override
    public <Result> Algorithm<Result> optimize(Algorithm<Result> node) {
        return node;
    }
    
    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> AccumulateNode optimize(AccumulateNode<R, E, O> node) {
        if (node.getLeft() instanceof TransformNode) {
            return optimizeAccumulateTransformNode(node, (TransformNode) optimize((TransformNode)node.getLeft()));
        }
        return node;
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>> TransformNode optimize(TransformNode<R, E, O> node) {
        if (node.getLeft() instanceof TransformNode) {
            return optimizeTransformTransformNode(node, (TransformNode) optimize((TransformNode)node.getLeft()));
        }
        return node;
    }

    private <R, R1, A, Aop extends IAccumulateOperation<R, R1>, Top extends ITransformOperation<R1, A>>
            AccumulateNode<R, A, AccumulateTransformOperation<R, R1, A>>
            optimizeAccumulateTransformNode(AccumulateNode<R, R1, Aop> aNode, TransformNode<R1, A, Top> tNode) {
        assert tNode == (TransformNode) aNode.getLeft();

        final Algorithm<List<A>> in = tNode.getLeft();

        final Algorithm<IAccumulateOperation<R, R1>> aop = (Algorithm<IAccumulateOperation<R, R1>>) aNode.getRight();
        final Algorithm<ITransformOperation<R1, A>> top = (Algorithm<ITransformOperation<R1, A>>) tNode.getRight();
        final AccumulateTransformOperationNode<R, R1, A> op = new AccumulateTransformOperationNode<>(aop, top);

        return new AccumulateNode<>(in, op);
    }

    private <R, R1, A, outOp extends ITransformOperation<R, R1>, inOp extends ITransformOperation<R1, A>>
            TransformNode<R, A, TransformTransformOperation<R, R1, A>>
            optimizeTransformTransformNode(TransformNode<R, R1, outOp> outerNode, TransformNode<R1, A, inOp> innerNode) {
        assert innerNode == (TransformNode) outerNode.getLeft();

        final Algorithm<List<A>> in = innerNode.getLeft();

        final Algorithm<ITransformOperation<R1, A>> inop = (Algorithm<ITransformOperation<R1, A>>) innerNode.getRight();
        final Algorithm<ITransformOperation<R, R1>> outop = (Algorithm<ITransformOperation<R, R1>>) outerNode.getRight();
        final TransformTransformOperationNode<R, R1, A> op = new TransformTransformOperationNode<>(outop, inop);

        return new TransformNode<>(in, op);
    }

}
