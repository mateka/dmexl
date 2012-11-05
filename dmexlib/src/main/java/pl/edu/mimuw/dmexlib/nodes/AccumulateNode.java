/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
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
    public Result execute(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        List<Element> aResult = ctx.getExecutor().execute(getA(), ctx);
        Operation op = ctx.getExecutor().execute(getB(), ctx);
        Result zero = ctx.getExecutor().execute(getC(), ctx);

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult);
        // Do sequential accumulation
        Result result = zero;
        while (elements.hasNext()) {
            result = op.invoke(result, op.invoke(elements.next()));
        }
        // TODO make copy of zero?
        return result;
    }

    @Override
    public Result accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }
}
