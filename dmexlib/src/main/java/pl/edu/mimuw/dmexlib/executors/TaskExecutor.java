/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TaskExecutor implements IExecutor {

    public TaskExecutor(int nThreads) {
        if (nThreads < 2) {
            throw new IllegalArgumentException("nThreads < 2");
        }

        workersNumber = nThreads;
        execService = Executors.newFixedThreadPool(workersNumber);
    }

    public ExecutorService getExecService() {
        return execService;
    }

    @Override
    public <T> T execute(IdentityNode<T> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }

    @Override
    public <T, F extends IFilterOperation<T>> List<T> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        List<Future<R>> tasks = null;
        try {
            Future<List<E>> left = createTask(algo.getLeft(), ctx);
            Future<O> right = createTask(algo.getRight(), ctx);

            // Get data for accumulate algorithm
            Iterator<E> elements = ctx.iterator(left.get());
            O op = right.get();

            // Submit algorithm's tasks
            int size = 0;
            CompletionService<R> ecs = new ExecutorCompletionService<>(getExecService());
            tasks = new LinkedList<>();
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

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        final List<E> coll = algo.getA().accept(ctx);
        final O op = algo.getB().accept(ctx);
        final R zero = algo.getC().accept(ctx);
        final int threshold = Math.max(getWorkersNumber(), coll.size() / getWorkersNumber());


        if (coll.size() > threshold) {
            final int power = 1 + (int)(Math.log(coll.size()/threshold) / Math.log(2));
            final int partSize = coll.size() / (int)Math.pow(2, power);
//            int partSize = coll.size() / 2; // coll.size / (2^(log2(coll.size/threshold)))
//            while (partSize > threshold) {
//                partSize = partSize / 2;
//            }

            final List<Future<R>> tasks = new LinkedList<>();
            try {
                final CompletionService<R> ecs = new ExecutorCompletionService<>(getExecService());
                for (int i = 0; i < coll.size(); i += partSize) {
                    final int begin = i;
                    final int end = Math.min(i + partSize, coll.size());
                    tasks.add(ecs.submit(new Callable<R>() {
                        @Override
                        public R call() throws Exception {
                            return sequentiaAccumulate(coll.subList(begin, end), op, zero);
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
            return sequentiaAccumulate(coll, op, zero);
        }

    }

    @Override
    public <Result> Result execute(Algorithm< Result> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }

    @Override
    public void shutdown() {
        getExecService().shutdownNow();
    }

    protected <R> Future<R> createTask(final Algorithm<R> algo, final IExecutionContext ctx) {
        return getExecService().submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                return algo.accept(ctx);
            }
        });
    }

    private <R, E, O extends IAccumulateOperation<R, E>> R sequentiaAccumulate(List<E> coll, O op, R zero) {
        Iterator<E> elements = coll.iterator();
        R result = zero;
        while (elements.hasNext()) {
            result = op.invoke(result, op.invoke(elements.next()));
        }
        return result;
    }

    private <R, E, O extends IAccumulateOperation<R, E>> R treeAccumulate(final List<Future<R>> inTasks, final O op) throws Exception {
        try {
            if (1 == inTasks.size()) {
                return inTasks.get(0).get();
            } else {
                final List<Future<R>> tasks = new LinkedList<>();
                try {
                    final CompletionService<R> ecs = new ExecutorCompletionService<>(getExecService());

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

    private int getWorkersNumber() {
        return workersNumber;
    }
    private ExecutorService execService;
    private int workersNumber;
}
