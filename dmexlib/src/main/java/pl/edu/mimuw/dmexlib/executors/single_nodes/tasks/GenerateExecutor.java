/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.tasks;

import java.util.List;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IGenerateExecutor;
import pl.edu.mimuw.dmexlib.nodes.GenerateNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;

/**
 *
 * @author matek
 */
public class GenerateExecutor implements IGenerateExecutor {

    public GenerateExecutor(IExecutorTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public <E, O extends IGenerateOperation<E>> List<E> execute(GenerateNode<E, O> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }
    
    private IExecutorTaskManager taskManager;
}
