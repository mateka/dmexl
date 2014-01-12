/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.tasks;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.ITransformExecutor;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TransformExecutor implements ITransformExecutor {

    public TransformExecutor(IExecutorTaskManager taskManager) {
        this.taskManager = taskManager;
    }
    
    @Override
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        List<Future<R>> tasks = null;
        try {
            Future<List<E>> left = taskManager.createAlgorithmTask(algo.getLeft(), ctx);
            Future<O> right = taskManager.createAlgorithmTask(algo.getRight(), ctx);

            // Get data for accumulate algorithm
            Iterator<E> elements = ctx.iterator(left.get());
            O op = right.get();

            // Submit algorithm's tasks
            int size = 0;
            CompletionService<R> ecs = taskManager.getCompletionService();
            tasks = new LinkedList<Future<R>>();
            while (elements.hasNext()) {
                final E current = elements.next();
                final O fun = op;
                ++size;

                tasks.add(ecs.submit(new Callable<R>() {
                    @Override
                    public R call() throws Exception {
                        return fun.invoke(current);
                    }
                }));
            }
            // Consume tasks' results
            List<R> resultElements = algo.createNewCollection();
            for (int i = 0; i < size; ++i) {
                resultElements.add(ecs.take().get());
            }
            return resultElements;
        } catch (ExecutionException execEx) {
            if (null != execEx.getCause() && execEx.getCause() instanceof Exception) {
                throw (Exception) execEx.getCause();
            } else {
                throw execEx;
            }
        } finally {
            if (null != tasks) {
                for (Future f : tasks) {
                    f.cancel(true);
                }
            }
        }
    }
    
    private final IExecutorTaskManager taskManager;
}
