/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.dummy;

import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IAccumulateExecutor;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;

/**
 *
 * @author matek
 */
public class AccumulateExecutor implements IAccumulateExecutor {

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }
    
}
