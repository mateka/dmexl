/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.LazyList;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;

/**
 *
 * @author matek
 */
public class GenerateNode<Element, Operation extends IGenerateOperation<Element>>
        extends BinaryNode<List<Element>, Algorithm<Integer>, Algorithm<Operation>> {

    public GenerateNode(Algorithm<Integer> left, Algorithm<Operation> right) {
        super(left, right);
    }

    @Override
    public List<Element> execute(IExecutionContext ctx) throws Exception {
        Integer count = ctx.getExecutor().execute(getLeft(), ctx);
        Operation op = ctx.getExecutor().execute(getRight(), ctx);
        
        return new LazyList<>(count, op);
    }

    @Override
    public List<Element> accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }

}
