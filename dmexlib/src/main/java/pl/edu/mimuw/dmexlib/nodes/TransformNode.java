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
    public List<Result> execute(IExecutionContext ctx) throws Exception {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        List<Element> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        Operation op = ctx.getExecutor().execute(getRight(), ctx);

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult);

        // Do sequential algorithm
        boolean ok = true;
        List<Result> resultElements = createNewCollection();
        while (ok && elements.hasNext()) {
            resultElements.add(op.invoke(elements.next()));
        }
        
        return resultElements;
    }
    
    @Override
    public List<Result> accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }
    
    public List<Result> createNewCollection() {
        return new ArrayList<Result>();
    }
}
