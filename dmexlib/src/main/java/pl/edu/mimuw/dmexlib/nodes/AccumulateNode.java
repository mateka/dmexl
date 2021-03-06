/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.Iterator;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;

/**
 *
 * @author matek
 */
public class AccumulateNode<Result, Element, Operation extends IAccumulateOperation<Result, Element>>
        extends BinaryNode<Result, Algorithm<List<Element>>, Algorithm<Operation>> {

    public AccumulateNode(Algorithm<List<Element>> elementsAlgo, Algorithm<Operation> opAlgo) {
        super(elementsAlgo, opAlgo);
    }

    @Override
    public Result execute(IExecutionContext ctx) throws Exception {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        List<Element> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        Operation op = ctx.getExecutor().execute(getRight(), ctx);

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult);
        // Do sequential accumulation
        Result result = null;
        if (elements.hasNext()) {
            result = op.invoke(elements.next());
            while (elements.hasNext()) {
                result = op.invoke(result, op.invoke(elements.next()));
            }
        }
        return result;
    }

    @Override
    public Result accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }
}
