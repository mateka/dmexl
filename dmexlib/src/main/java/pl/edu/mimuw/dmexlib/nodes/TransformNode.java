/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.utils.ResultType;

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
    public IResultType<List<Result>> execute(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        // Calculate results in subtrees. Check for errors to stop calculations
        // as early as possible.
        IResultType<List<Element>> aResult = ctx.getExecutor().execute(getLeft(), ctx);
        if (!aResult.isOk()) {
            return new ResultType<>(null, false);
        }
        
        IResultType<Operation> bResult = ctx.getExecutor().execute(getRight(), ctx);
        if (!bResult.isOk()) {
            aResult.cancel(true);
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<Element> elements = ctx.iterator(aResult.get());
        Operation op = bResult.get();

        // Do sequential algorithm
        boolean ok = true;
        List<Result> resultElements = createNewCollection();
        while (ok && elements.hasNext()) {
            ResultType<Result> res = op.invoke(elements.next());
            if (res.isOk()) {
                resultElements.add(res.get());
            } else {
                ok = false;
            }
        }
        
        return new ResultType<>(resultElements, ok);
    }
    
    @Override
    public IResultType<List<Result>> accept(IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return ctx.getExecutor().execute(this, ctx);
    }
    
    public List<Result> createNewCollection() {
        return new ArrayList<>();
    }
}
