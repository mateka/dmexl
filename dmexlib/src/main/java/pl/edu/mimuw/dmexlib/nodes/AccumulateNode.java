/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;

/**
 *
 * @author matek
 */
public class AccumulateNode<Result, Element, Operation extends IAccumulateOperation<Result, Element>>
        extends TernaryNode<Result, Algorithm<Iterable<Element>>, Algorithm<Operation>, Algorithm<Result>> {

    public AccumulateNode(Algorithm<Iterable<Element>> elementsAlgo, Algorithm<Operation> opAlgo, Algorithm<Result> zeroAlgo) {
        super(elementsAlgo, opAlgo, zeroAlgo);
    }

    @Override
    public ResultType<Result> execute(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        ResultType<Iterable<Element>> aResult = ctx.getExecutor().execute(getA(), ctx);
        if (!aResult.isOk()) return new ResultType<>(null, false);

        ResultType<Operation> bResult = ctx.getExecutor().execute(getB(), ctx);
        if (!bResult.isOk()) return new ResultType<>(null, false);

        ResultType<Result> cResult = ctx.getExecutor().execute(getC(), ctx);
        if (!cResult.isOk()) return new ResultType<>(null, false);

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult.get());
        Operation op = bResult.get();
        Result zero = cResult.get();

        // Do sequential accumulation
        boolean ok = true;
        while (ok && elements.hasNext()) {
            ResultType<Result> res = op.invoke(zero, elements.next());
            if (res.isOk()) zero = res.get();
            else ok = false;
        }
        // TODO make copy of zero?
        return new ResultType<>(zero, ok);
    }

    @Override
    public ResultType<Result> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }
}
