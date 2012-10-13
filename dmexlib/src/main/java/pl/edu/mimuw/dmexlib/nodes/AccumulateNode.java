/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.utils.ResultType;

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
    public IResultType<Result> execute(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        IResultType<List<Element>> aResult = ctx.getExecutor().execute(getA(), ctx);
        if (!aResult.isOk()) {
            return new ResultType<>(null, false);
        }

        IResultType<Operation> bResult = ctx.getExecutor().execute(getB(), ctx);
        if (!bResult.isOk()) {
            aResult.cancel(true);
            return new ResultType<>(null, false);
        }

        IResultType<Result> cResult = ctx.getExecutor().execute(getC(), ctx);
        if (!cResult.isOk()) {
            aResult.cancel(true);
            bResult.cancel(true);
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult.get());
        Operation op = bResult.get();
        Result zero = cResult.get();

        // Do sequential accumulation
        boolean ok = true;
        while (ok && elements.hasNext()) {
            ResultType<Result> argToResult = op.invoke(elements.next());
            if (argToResult.isOk()) {
                ResultType<Result> res = op.invoke(zero, argToResult.get());
                if (res.isOk()) {
                    zero = res.get();
                } else {
                    ok = false;
                }
            } else {
                ok = false;
            }
        }
        // TODO make copy of zero?
        return new ResultType<>(zero, ok);
    }

    @Override
    public IResultType<Result> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }
}
