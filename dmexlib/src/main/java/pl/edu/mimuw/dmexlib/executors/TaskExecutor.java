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
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.utils.FutureResultType;
import pl.edu.mimuw.dmexlib.utils.ResultType;
import pl.edu.mimuw.dmexlib.utils.TreeFutureResultType;

/**
 *
 * @author matek
 */
public class TaskExecutor implements IExecutor {

    public TaskExecutor(int nThreads) {
        if(nThreads < 2) {
            throw new IllegalArgumentException("nThreads < 2");
        }
            
        workersNumber = nThreads;
        execService = Executors.newFixedThreadPool(workersNumber);
    }

    public synchronized ExecutorService getExecService() {
        return execService;
    }

    @Override
    public <T> IResultType<T> execute(IdentityNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return algo.execute(ctx);
    }

    @Override
    public <T, F extends IFilterOperation<T>> IResultType<List<T>> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>> IResultType<List<R>> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        FutureResultType<List<E>> left = createTask(algo.getLeft(), ctx);
        FutureResultType<O> right = createTask(algo.getRight(), ctx);

        if (!left.isOk() || !right.isOk()) {
            left.cancel(true);
            right.cancel(true);
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<E> elements = ctx.iterator(left.get());
        O op = right.get();

        // Submit algorithm's tasks
        int size = 0;
        CompletionService<ResultType<R>> ecs = new ExecutorCompletionService<>(getExecService());
        List<Future<ResultType<R>>> tasks = new LinkedList<>();
        while (elements.hasNext()) {
            final E current = elements.next();
            final O fun = op;
            ++size;

            tasks.add(ecs.submit(new Callable<ResultType<R>>() {
                @Override
                public ResultType<R> call() throws InterruptedException, ExecutionException {
                    return fun.invoke(current);
                }
            }));
        }
        // Consume tasks' results
        boolean ok = true;
        List<R> resultElements = algo.createNewCollection();
        for (int i = 0; i < size && ok; ++i) {
            ResultType<R> res = ecs.take().get();
            if (res.isOk()) {
                resultElements.add(res.get());
            } else {
                ok = false;
                for (Future f : tasks) {
                    f.cancel(true);
                }
            }
        }

        return new ResultType<>(resultElements, ok);
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> IResultType<R> execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        FutureResultType<List<E>> aPart = createTask(algo.getA(), ctx);
        FutureResultType<O> bPart = createTask(algo.getB(), ctx);
        FutureResultType<R> cPart = createTask(algo.getC(), ctx);

        if (!aPart.isOk() || !bPart.isOk() || !cPart.isOk()) {
            aPart.cancel(true);
            bPart.cancel(true);
            cPart.cancel(true);
            return new ResultType<>(null, false);
        }

        List<E> coll = aPart.get();
        int threshold = Math.max(getWorkersNumber(), coll.size()/getWorkersNumber());
        IResultType<R> res = accumulateInSplit(coll, bPart.get(), cPart.get(), ctx, threshold);

        return res;
    }

    @Override
    public <Result> IResultType<Result> execute(Algorithm<Result> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return algo.execute(ctx);
    }
    
    @Override
    public void shutdown() {
        getExecService().shutdownNow();
    }

    protected <R> FutureResultType<R> createTask(final Algorithm<R> algo, final IExecutionContext ctx) {
        return new FutureResultType(getExecService().submit(
                new Callable<IResultType<R>>() {
                    @Override
                    public IResultType<R> call() throws InterruptedException, ExecutionException {
                        return algo.accept(ctx);
                    }
                }));
    }
    
    // TODO make copy of zero
    private <R, E, O extends IAccumulateOperation<R, E>> IResultType<R> accumulateInSplit(final List<E> coll, final O op, final R zero, final IExecutionContext ctx, final int splitThreshold) throws InterruptedException, ExecutionException {
        if (coll.size() > splitThreshold) {
            final List<E> left = coll.subList(0, coll.size()/2);
            final List<E> right = coll.subList(coll.size()/2, coll.size());
            
            final IResultType<R> lresult = new FutureResultType(getExecService().submit(
                    new Callable<IResultType<R>>() {
                        @Override
                        public IResultType<R> call() throws InterruptedException, ExecutionException {
                            IResultType<R> r = accumulateInSplit(left, op, zero, ctx, splitThreshold);
                            return r;
                        }
                    }));
            final IResultType<R> rresult = new FutureResultType(getExecService().submit(
                    new Callable<IResultType<R>>() {
                        @Override
                        public IResultType<R> call() throws InterruptedException, ExecutionException {
                            IResultType<R> r = accumulateInSplit(right, op, zero, ctx, splitThreshold);
                            return r;
                        }
                    }));

            return new TreeFutureResultType<>(getExecService().submit(
                    new Callable<IResultType<R>>() {
                        @Override
                        public IResultType<R> call() throws InterruptedException, ExecutionException {
                            IResultType<R> r = op.invoke(lresult.get(), rresult.get());
                            return r;
                        }
                    }), lresult, rresult);
        } else {
            Iterator<E> elements = coll.iterator();
            ResultType<R> result = new ResultType<>(zero, true);
            while (result.isOk() && elements.hasNext()) {
                ResultType<R> argToResult = op.invoke(elements.next());
                if (argToResult.isOk()) {
                    result = op.invoke(result.get(), argToResult.get());
                } else {
                    result = new ResultType<>(null, false);
                }
            }
            return result;
        }
    }
    
    private int getWorkersNumber() {
        return workersNumber;
    }
    
    private ExecutorService execService;
    private int workersNumber;

}
