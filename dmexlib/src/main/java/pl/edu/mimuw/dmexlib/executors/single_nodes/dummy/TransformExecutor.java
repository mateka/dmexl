/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.dummy;

import java.util.List;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.ITransformExecutor;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TransformExecutor implements ITransformExecutor {

    @Override
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }
}
