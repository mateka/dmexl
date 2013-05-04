/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.tasks;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;

/**
 *
 * @author matek
 */
public interface IExecutorTaskManager {
    
    public <R> Future<R> createAlgorithmTask(final Algorithm<R> algo, final IExecutionContext ctx);

    public <R> CompletionService<R> getCompletionService();

    public int getWorkersNumber();

    public void shutdown();
}
