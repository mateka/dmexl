/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.Collection;
import java.util.Iterator;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public abstract class TransformNode<Result, Element, Operation extends ITransformOperation<Result, Element>>
        extends BinaryNode<Collection<Result>, Algorithm<Iterable<Element>>, Algorithm<Operation>> {

    public TransformNode(Algorithm<Iterable<Element>> left, Algorithm<Operation> right) {
        super(left, right);
    }

    @Override
    public ResultType<Collection<Result>> sequentialExecute(IExecutionContext ctx) {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        ResultType<Iterable<Element>> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        if (!aResult.isOk()) {
            return new ResultType<>(null, false);
        }

        ResultType<Operation> bResult = ctx.getExecutor().execute(getRight(), ctx);
        if (!bResult.isOk()) {
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult.getResult());
        Operation op = bResult.getResult();

        // Do sequential algorithm
        boolean ok = true;
        Collection<Result> resultElements = createNewCollection();
        while (ok && elements.hasNext()) {
            ResultType<Result> res = op.invoke(elements.next());
            if (res.isOk()) resultElements.add(res.getResult());
            else ok = false;
        }

        return new ResultType<>(resultElements, ok);
    }

    @Override
    public ResultType<Collection<Result>> multiCPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultType<Collection<Result>> GPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected abstract Collection<Result> createNewCollection();
}
