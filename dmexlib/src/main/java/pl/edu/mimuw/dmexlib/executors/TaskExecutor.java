/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.FutureResultType;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.SetNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;

/**
 *
 * @author matek
 */
public class TaskExecutor extends OptimizingExecutor {
    
    public TaskExecutor(ITreeOptimizer treeOptimizer) {
        super(treeOptimizer);
        execService = java.util.concurrent.Executors.newFixedThreadPool(2);
    }
    
    public ExecutorService getExecService() {
        return execService;
    }
    
    @Override
    public <T> ResultType<T> execute(IdentityNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return getOptimizer().optimize(algo).execute(ctx);
    }
    
    @Override
    public <T> ResultType<Set<T>> execute(SetNode<T> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public <T, F extends IFilterOperation<T>, C extends Collection<T>> ResultType<C> execute(FilterNode<T, F, C> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public <R, E, O extends ITransformOperation<R, E>, C extends Collection<R>> ResultType<C> execute(TransformNode<R, E, O, C> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        TransformNode<R, E, O, C> optAlgo = getOptimizer().optimize(algo);
        
        ResultType<Iterable<E>> left = ctx.getExecutor().execute(optAlgo.getLeft(), ctx);
        if (!left.isOk()) {
            return new ResultType<>(null, false);
        }
        
        ResultType<O> right = ctx.getExecutor().execute(optAlgo.getRight(), ctx);
        if (!right.isOk()) {
            left.cancel(true);
            return new ResultType<>(null, false);
        }
        
//        FutureResultType<Iterable<E>> left = createTask(optAlgo.getLeft(), ctx);
//        FutureResultType<O> right = createTask(optAlgo.getRight(), ctx);
//        
//        if (!left.isOk() || !right.isOk()) {
//            left.cancel(true);
//            right.cancel(true);
//            return new ResultType<>(null, false);
//        }

        // Get data for accumulate algorithm
        Iterator<E> elements = ctx.iterator(left.get());
        O op = right.get();
        
        boolean ok = true;
        C resultElements = optAlgo.createNewCollection();
        while (ok && elements.hasNext()) {
            ResultType<R> res = op.invoke(elements.next());
            if (res.isOk()) {
                resultElements.add(res.get());
            } else {
                ok = false;
            }
        }
        
        return new ResultType<>(resultElements, ok);

        // Submit algorithm's tasks
//        int size = 0;
//        CompletionService<ResultType<R>> ecs = new ExecutorCompletionService<>(getExecService());
//        List<Future<ResultType<R>>> tasks = new LinkedList<>();
//        while (elements.hasNext()) {
//            final E current = elements.next();
//            final O fun = op;
//            ++size;
//            
//            tasks.add(ecs.submit(new Callable<ResultType<R>>() {
//                @Override
//                public ResultType<R> call() throws InterruptedException, ExecutionException {
//                    return fun.invoke(current);
//                }
//            }));
//        }
//        // Consume tasks' results
//        boolean ok = true;
//        C resultElements = optAlgo.createNewCollection();
//        for (int i = 0; i < size && ok; ++i) {
//            ResultType<R> res = ecs.take().get();
//            if (res.isOk()) {
//                resultElements.add(res.get());
//            } else {
//                ok = false;
//                for (Future f : tasks) {
//                    f.cancel(true);
//                }
//            }
//        }
//        
//        return new ResultType<>(resultElements, ok);
    }
    
    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> ResultType<R> execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        AccumulateNode<R, E, O> optAlgo = getOptimizer().optimize(algo);

        FutureResultType<Iterable<E>> aPart = createTask(optAlgo.getA(), ctx);
        FutureResultType<O> bPart = createTask(optAlgo.getB(), ctx);
        FutureResultType<R> cPart = createTask(optAlgo.getC(), ctx);
        
        if (!aPart.isOk() || !bPart.isOk() || !cPart.isOk()) {
            aPart.cancel(true);
            bPart.cancel(true);
            cPart.cancel(true);
            return new ResultType<>(null, false);
        }

        // Get data for accumulate algorithm
        Iterator<E> elements = ctx.iterator(aPart.get());
        O op = bPart.get();
        R zero = cPart.get();

        // Do accumulation in tasks
        //CompletionService<Result> ecs = new ExecutorCompletionService<Result>(e);
        boolean ok = true;
        while (ok && elements.hasNext()) {
            ResultType<R> res = op.invoke(zero, elements.next());
            if (res.isOk()) {
                zero = res.get();
            } else {
                ok = false;
            }
        }
        // TODO make copy of zero?
        return new ResultType<>(zero, ok);
    }
    
    @Override
    public <Result> ResultType<Result> execute(Algorithm<Result> algo, IExecutionContext ctx) throws InterruptedException, ExecutionException {
        return getOptimizer().optimize(algo).execute(ctx);
    }
    
    protected <R> FutureResultType<R> createTask(final Algorithm<R> algo, final IExecutionContext ctx) {
        return new FutureResultType(getExecService().submit(
                new Callable<ResultType<R>>() {
                    @Override
                    public ResultType<R> call() throws InterruptedException, ExecutionException {
                        return algo.accept(ctx);
                    }
                }));
    }
    private ExecutorService execService;
}
