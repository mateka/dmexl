/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import java.util.Iterator;
import pl.edu.mimuw.dmexlib.executors.IExecutor;

/**
 * Base interface for algorithm's execution context
 * TODO Make a project for it
 * @author matek
 */
public interface IExecutionContext {
    public abstract IExecutor getExecutor();
    public abstract <T> Iterator<T> iterator(Iterable<T> i);
}
