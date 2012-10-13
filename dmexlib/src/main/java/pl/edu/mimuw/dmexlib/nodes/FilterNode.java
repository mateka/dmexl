/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.utils.ResultType;

/**
 *
 * @author matek
 */
public class FilterNode<Type, Filter extends IFilterOperation<Type>>
        extends BinaryNode<List<Type>, Algorithm<List<Type>>, Algorithm<Filter>> {

    public FilterNode(Algorithm<List<Type>> elements, Algorithm<Filter> filter) {
        super(elements, filter);
    }

    @Override
    public IResultType<List<Type>> execute(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        IResultType<List<Type>> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        if (!aResult.isOk()) {
            return new ResultType<>(null, false);
        }

        IResultType<Filter> bResult = ctx.getExecutor().execute(getRight(), ctx);
        if (!bResult.isOk()) {
            aResult.cancel(true);
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<Type> elements = ctx.iterator(aResult.get());
        Filter op = bResult.get();

        // Do sequential algorithm
        List<Type> resultElements = createNewCollection();
        while (elements.hasNext()) {
            Type e = elements.next();
            if (op.invoke(e)) {
                resultElements.add(e);
            }
        }

        return new ResultType<>(resultElements);
    }

    @Override
    public IResultType<List<Type>> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }

    public List<Type> createNewCollection() {
        return new ArrayList<>();
    }
}
