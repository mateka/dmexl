/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;

/**
 *
 * @author matek
 */
public class AccumulateNode<Result, Element, Operation extends IAccumulateOperation<Result, Element>>
        extends TernaryNode<Result, Algorithm<List<Element>>, Algorithm<Operation>, Algorithm<Result>> {

    public AccumulateNode(Algorithm<List<Element>> elementsAlgo, Algorithm<Operation> opAlgo, Algorithm<Result> zeroAlgo) {
        super(elementsAlgo, opAlgo, zeroAlgo);
    }

    @Override
    public ResultType<Result> sequentialExecute(IExecutionContext ctx) {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        ResultType<List<Element>> aResult = ctx.getExecutor().execute(getA(), ctx);
        if (!aResult.isOk()) return new ResultType<>(null, false);

        ResultType<Operation> bResult = ctx.getExecutor().execute(getB(), ctx);
        if (!bResult.isOk()) return new ResultType<>(null, false);

        ResultType<Result> cResult = ctx.getExecutor().execute(getC(), ctx);
        if (!cResult.isOk()) return new ResultType<>(null, false);

        // Get data for accumulate algorithm
        List<Element> elements = aResult.getResult();
        Operation op = bResult.getResult();
        Result zero = cResult.getResult();

        // Do sequential accumulation
        final int left = ctx.getDomainPart().getLeft(elements);
        final int right = ctx.getDomainPart().getRight(elements);

        boolean ok = true;
        for (int i = left; ok && (i < right); ++i) {
            ResultType<Result> res = op.invoke(zero, elements.get(i));
            if (res.isOk()) zero = res.getResult();
            else ok = false;
        }
        // TODO make copy of zero?
        return new ResultType<>(zero, ok);
    }

    @Override
    public ResultType<Result> multiCPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultType<Result> GPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
