/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IAccumulateExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IFilterExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IGenerateExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.ITransformExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.AccumulateExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.FilterExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.GenerateExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.TransformExecutor;
import pl.edu.mimuw.dmexlib.nodes.AccumulateNode;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.GenerateNode;
import pl.edu.mimuw.dmexlib.nodes.IdentityNode;
import pl.edu.mimuw.dmexlib.nodes.TransformNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class CustomizableTaskExecutor implements IExecutor {

    public CustomizableTaskExecutor(int nThreads) {
        this(nThreads, new AccumulateExecutor(), new TransformExecutor(), new FilterExecutor(), new GenerateExecutor());
    }

    public CustomizableTaskExecutor(int nThreads, IAccumulateExecutor accumulateExecutor, ITransformExecutor transformExecutor, IFilterExecutor filterExecutor, IGenerateExecutor generateExecutor) {
        if (nThreads < 2) {
            throw new IllegalArgumentException("nThreads < 2");
        }
        this.workersNumber = nThreads;
        execService = Executors.newFixedThreadPool(workersNumber);
        
        this.accumulateExecutor = accumulateExecutor;
        this.transformExecutor = transformExecutor;
        this.filterExecutor = filterExecutor;
        this.generateExecutor = generateExecutor;
    }

    public void setAccumulateExecutor(IAccumulateExecutor accumulateExecutor) {
        this.accumulateExecutor = accumulateExecutor;
    }

    public void setTransformExecutor(ITransformExecutor transformExecutor) {
        this.transformExecutor = transformExecutor;
    }

    public void setFilterExecutor(IFilterExecutor filterExecutor) {
        this.filterExecutor = filterExecutor;
    }

    public void setGenerateExecutor(IGenerateExecutor generateExecutor) {
        this.generateExecutor = generateExecutor;
    }
    
    public ExecutorService getExecService() {
        return execService;
    }
    
    public int getWorkersNumber() {
        return workersNumber;
    }
    
    @Override
    public <T> T execute(IdentityNode<T> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }

    @Override
    public <T, F extends IFilterOperation<T>> List<T> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws Exception {
        return filterExecutor.execute(algo, ctx);
    }

    @Override
    public <E, O extends IGenerateOperation<E>> List<E> execute(GenerateNode<E, O> algo, IExecutionContext ctx) throws Exception {
        return generateExecutor.execute(algo, ctx);
    }

    @Override
    public <R, E, O extends ITransformOperation<R, E>> List<R> execute(TransformNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        return transformExecutor.execute(algo, ctx);
    }

    @Override
    public <R, E, O extends IAccumulateOperation<R, E>> R execute(AccumulateNode<R, E, O> algo, IExecutionContext ctx) throws Exception {
        return accumulateExecutor.execute(algo, ctx);
    }

    @Override
    public <Result> Result execute(Algorithm<Result> algo, IExecutionContext ctx) throws Exception {
        return algo.execute(ctx);
    }

    @Override
    public void shutdown() {
        getExecService().shutdownNow();
    }
    
    private ExecutorService execService;
    private int workersNumber;
    // Executors for one node
    IAccumulateExecutor accumulateExecutor;
    ITransformExecutor transformExecutor;
    IFilterExecutor filterExecutor;
    IGenerateExecutor generateExecutor;
}
