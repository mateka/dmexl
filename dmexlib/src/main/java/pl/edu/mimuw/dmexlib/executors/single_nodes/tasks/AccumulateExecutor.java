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
import pl.edu.mimuw.dmexlib.executors.single_nodes.IAccumulateExecutor;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;

/**
 *
 * @author matek
 */
public class AccumulateExecutor implements IAccumulateExecutor {

    public AccumulateExecutor(IExecutorTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        final List<E> coll = algo.getLeft().accept(ctx);
        final O op = algo.getRight().accept(ctx);
        final int threshold = Math.max(taskManager.getWorkersNumber(), coll.size() / taskManager.getWorkersNumber());

        if (coll.size() > threshold) {
            final int power = 1 + (int) (Math.log(coll.size() / threshold) / Math.log(2));
            final int partSize = coll.size() / (int) Math.pow(2, power);
            final List<Future<R>> tasks = new LinkedList<Future<R>>();
            try {
                final CompletionService<R> ecs = taskManager.getCompletionService();
                for (int i = 0; i < coll.size(); i += partSize) {
                    final int begin = i;
                    final int end = Math.min(i + partSize, coll.size());
                    tasks.add(ecs.submit(new Callable<R>() {
                        @Override
                        public R call() throws Exception {
                            return sequentiaAccumulate(coll.subList(begin, end), op);
                        }
                    }));
                }

                return treeAccumulate(tasks, op);
            } finally {
                for (Future f : tasks) {
                    f.cancel(true);
                }
            }
        } else {
            return sequentiaAccumulate(coll, op);
        }
    }
    
    private <R, E, O extends IAccumulateOperation<R, E>> R sequentiaAccumulate(List<E> coll, O op) {
        Iterator<E> elements = coll.iterator();
        R result = null;
        if (elements.hasNext()) {
            result = op.invoke(elements.next());
            while (elements.hasNext()) {
                result = op.invoke(result, op.invoke(elements.next()));
            }
        }
        return result;
    }

    private <R, E, O extends IAccumulateOperation<R, E>> R treeAccumulate(final List<Future<R>> inTasks, final O op) throws Exception {
        try {
            if (1 == inTasks.size()) {
                return inTasks.get(0).get();
            } else {
                final List<Future<R>> tasks = new LinkedList<Future<R>>();
                try {
                    final CompletionService<R> ecs = taskManager.getCompletionService();

                    int size = inTasks.size();
                    if (1 == inTasks.size() % 2) {
                        tasks.add(inTasks.get(inTasks.size() - 1));
                        --size;
                    }

                    for (int i = 0; i < size; i += 2) {
                        final int idx = i;
                        tasks.add(ecs.submit(new Callable<R>() {
                            @Override
                            public R call() throws Exception {
                                return op.invoke(inTasks.get(idx).get(), inTasks.get(idx + 1).get());
                            }
                        }));
                    }
                    return treeAccumulate(tasks, op);
                } finally {
                    for (Future f : tasks) {
                        f.cancel(true);
                    }
                }
            }
        } catch (ExecutionException execEx) {
            if (null != execEx.getCause() && execEx.getCause() instanceof Exception) {
                throw (Exception) execEx.getCause();
            } else {
                throw execEx;
            }
        }
    }
    
    private final IExecutorTaskManager taskManager;
}
