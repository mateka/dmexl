/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.ArrayList;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TransformNode<Result, Element, Operation extends ITransformOperation<Result, Element>>
        extends BinaryNode<List<Result>, Algorithm<List<Element>>, Algorithm<Operation>> {

    public TransformNode(Algorithm<List<Element>> left, Algorithm<Operation> right) {
        super(left, right);
    }

    @Override
    public ResultType<List<Result>> sequentialExecute(IExecutionContext ctx) {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        ResultType<List<Element>> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        if (!aResult.isOk()) {
            return new ResultType<>(null, false);
        }

        ResultType<Operation> bResult = ctx.getExecutor().execute(getRight(), ctx);
        if (!bResult.isOk()) {
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        List<Element> elements = aResult.getResult();
        Operation op = bResult.getResult();

        // Do sequential algorithm
        final int left = ctx.getDomainPart().getLeft(elements);
        final int right = ctx.getDomainPart().getRight(elements);

        boolean ok = true;
        List<Result> resultElements = new ArrayList<>(right - left);
        for (int i = left; ok && (i < right); ++i) {
            ResultType<Result> res = op.invoke(elements.get(i));
            if (res.isOk()) resultElements.add(i, res.getResult());
            else ok = false;
        }
        return new ResultType<>(resultElements, ok);
    }

    @Override
    public ResultType<List<Result>> multiCPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultType<List<Result>> GPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
