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
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.tasks.PrioritizedCompletionService;
import pl.edu.mimuw.dmexlib.executors.tasks.PriorityExecutorService;
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
        execService = new PriorityExecutorService(workersNumber);
    }

    public PriorityExecutorService getExecService() {
        return execService;
    }

    @Override
    public <T> T execute(IdentityNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        try {
            getExecService().incrementPriority();
            return algo.execute(ctx);
        } finally {
            getExecService().decrementPriority();
        }
    }

    @Override
    public <T, F extends IFilterOperation<T>> List<T> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        List<Future<R>> tasks = null;
        try {
            getExecService().incrementPriority();

            Future<List<E>> left = createTask(algo.getLeft(), ctx);
            Future<O> right = createTask(algo.getRight(), ctx);

            // Get data for accumulate algorithm
            Iterator<E> elements = ctx.iterator(left.get());
            O op = right.get();

            // Submit algorithm's tasks
            int size = 0;
            CompletionService<R> ecs = new PrioritizedCompletionService<>(getExecService());
            tasks = new LinkedList<>();
            while (elements.hasNext()) {
                final E current = elements.next();
                final O fun = op;
                ++size;

                tasks.add(ecs.submit(new Callable<R>() {
                    @Override
                    public R call() throws InterruptedException, ExecutionException {
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
        } finally {
            getExecService().decrementPriority();

            if (null != tasks) {
                for (Future f : tasks) {
                    f.cancel(true);
                }
            }
        }
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        try {
            getExecService().incrementPriority();

            //Future<List<E>> aPart = createTask(algo.getA(), ctx);
//            Future<O> bPart = createTask(algo.getB(), ctx);
//            Future<R> cPart = createTask(algo.getC(), ctx);

            List<E> coll = algo.getA().accept(ctx);//aPart.get();
            int threshold = Math.max(getWorkersNumber(), coll.size() / getWorkersNumber());
            //System.out.println("accumulate");
            //return accumulateInSplit(coll, bPart.get(), cPart.get(), ctx, threshold);
            return accumulateInSplit(coll, algo.getB().accept(ctx), algo.getC().accept(ctx), ctx, threshold);
        } finally {
            getExecService().decrementPriority();
        }
    }

    @Override
    public <Result> Result execute(Algorithm< Result> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return algo.execute(ctx);
    }

    @Override
    public void shutdown() {
        getExecService().shutdownNow();
    }

    protected <R> Future<R> createTask(final Algorithm<R> algo, final IExecutionContext ctx) {
        return getExecService().submit(new Callable<R>() {
            @Override
            public R call() throws InterruptedException, ExecutionException {
                return algo.accept(ctx);
            }
        });
    }

    private <R, E, O extends IAccumulateOperation<R, E>> R accumulateInSplit(final List<E> coll, final O op, final R zero, final IExecutionContext ctx, final int splitThreshold) throws InterruptedException, ExecutionException {
        //System.out.println("accumulate in split");
        if (coll.size() > splitThreshold) {
            //System.out.println("split");
            final List<E> left = coll.subList(0, coll.size() / 2);
            final List<E> right = coll.subList(coll.size() / 2, coll.size());

            boolean mustDecrement = true;
            try {
                getExecService().incrementPriority();

                final Future<R> lresult = getExecService().submit(new Callable<R>() {
                    @Override
                    public R call() throws InterruptedException, ExecutionException {
                        //System.out.println("left");
                        return accumulateInSplit(left, op, zero, ctx, splitThreshold);
                    }
                });
                final Future<R> rresult = getExecService().submit(new Callable<R>() {
                    @Override
                    public R call() throws InterruptedException, ExecutionException {
                       // System.out.println("right");
                        return accumulateInSplit(right, op, zero, ctx, splitThreshold);
                    }
                });

                getExecService().decrementPriority();
                mustDecrement = false;

                return getExecService().submit(
                        new Callable<R>() {
                            @Override
                            public R call() throws InterruptedException, ExecutionException {
                                //System.out.println("join");
                                R left = null;
                                try {
                                    left = lresult.get();
                                } catch (ExecutionException e) {
                                    rresult.cancel(true);
                                    throw e;
                                }
                                R right = null;
                                try {
                                    right = rresult.get();
                                } catch (ExecutionException e) {
                                    throw e;
                                }
                                return op.invoke(left, right);
                            }
                        }).get();
            } finally {
                if (mustDecrement) {
                    getExecService().decrementPriority();
                }
            }
        } else {
            //System.out.println("accumulate - seq part");
            Iterator<E> elements = coll.iterator();
            R result = zero;
            while (elements.hasNext()) {
                result = op.invoke(result, op.invoke(elements.next()));
            }
            return result;
        }
    }

    private int getWorkersNumber() {
        return workersNumber;
    }
    private PriorityExecutorService execService;
    private int workersNumber;
}
