/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.IResultType;
import pl.edu.mimuw.dmexlib.executors.IExecutor;

/**
 * Base interface for algorithm's execution context
 * TODO Make a project for it
 * @author matek
 */
public interface IExecutionContext {
    public <Result> IResultType<Result> execute(Algorithm<Result> algo) throws InterruptedException, ExecutionException;
    public IExecutor getExecutor();
    public <T> Iterator<T> iterator(List<T> i);
}
