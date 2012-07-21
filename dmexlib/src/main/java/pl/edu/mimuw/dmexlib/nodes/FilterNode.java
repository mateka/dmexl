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
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;

/**
 *
 * @author matek
 */
public abstract class FilterNode<Type, Filter extends IFilterOperation<Type>, CollectionType extends Collection<Type>>
        extends BinaryNode<CollectionType, Algorithm<Iterable<Type>>, Algorithm<Filter>> {

    public FilterNode(Algorithm<Iterable<Type>> elements, Algorithm<Filter> filter) {
        super(elements, filter);
    }

    @Override
    public ResultType<CollectionType> sequentialExecute(IExecutionContext ctx) {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        ResultType<Iterable<Type>> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        if (!aResult.isOk()) {
            return new ResultType<>(null, false);
        }

        ResultType<Filter> bResult = ctx.getExecutor().execute(getRight(), ctx);
        if (!bResult.isOk()) {
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<Type> elements = ctx.iterator(aResult.getResult());
        Filter op = bResult.getResult();

        // Do sequential algorithm
        CollectionType resultElements = createNewCollection();
        while (elements.hasNext()) {
            Type e = elements.next();
            if(op.invoke(e)) resultElements.add(e);
        }

        return new ResultType<>(resultElements);
    }

    @Override
    public ResultType<CollectionType> multiCPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResultType<CollectionType> GPUExecute(IExecutionContext ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected abstract CollectionType createNewCollection();
}
