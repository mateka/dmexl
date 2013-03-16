/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;

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
    public List<Type> execute(IExecutionContext ctx) throws Exception {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        List<Type> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        Filter op = ctx.getExecutor().execute(getRight(), ctx);

        // Get data for accumulate algorithm
        Iterator<Type> elements = ctx.iterator(aResult);

        // Do sequential algorithm
        List<Type> resultElements = createNewCollection();
        while (elements.hasNext()) {
            Type e = elements.next();
            if (op.invoke(e)) {
                resultElements.add(e);
            }
        }

        return resultElements;
    }

    @Override
    public List<Type> accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }

    public List<Type> createNewCollection() {
        return new ArrayList<Type>();
    }
}
