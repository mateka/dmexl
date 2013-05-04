/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.tasks.IExecutorTaskManager;

/**
 *
 * @author matek
 */
public class SimpleTaskManager implements IExecutorTaskManager {

    public SimpleTaskManager(int workersNumber) {
        if (workersNumber < 2) {
            throw new IllegalArgumentException("workersNumber < 2");
        }
        
        this.workersNumber = workersNumber;
        this.execService = Executors.newFixedThreadPool(workersNumber);
    }

    @Override
    public <R> CompletionService<R> getCompletionService() {
        return new ExecutorCompletionService<R>(execService);
    }
    
    @Override
    public <R> Future<R> createAlgorithmTask(final Algorithm<R> algo, final IExecutionContext ctx) {
        return execService.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                return algo.accept(ctx);
            }
        });
    }

    @Override
    public int getWorkersNumber() {
        return workersNumber;
    }

    @Override
    public void shutdown() {
        execService.shutdownNow();
    }
    private ExecutorService execService;
    private int workersNumber;
}
