/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 * @author matek
 */
public class PrioritizedRunnableFuture<T>
        extends FutureTask<T>
        implements Comparable<PrioritizedRunnableFuture<T>> {

    public PrioritizedRunnableFuture(Callable<T> callable, final int priority) {
        super(callable);
        this.priority = priority;
    }

    public PrioritizedRunnableFuture(Runnable runnable, T result, final int priority) {
        super(runnable, result);
        this.priority = priority;
    }
    
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PrioritizedRunnableFuture<T> o) {
        return Integer.compare(priority, o.getPriority());
    }
    private final int priority;
}