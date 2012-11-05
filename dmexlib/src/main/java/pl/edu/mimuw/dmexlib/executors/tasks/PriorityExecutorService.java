/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.tasks;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author matek
 */
public class PriorityExecutorService extends ThreadPoolExecutor {

    public PriorityExecutorService(int poolSize) {
        super(poolSize, poolSize, Long.MAX_VALUE, TimeUnit.NANOSECONDS,
                new PriorityBlockingQueue<Runnable>(20, new ComparePriority()));
        currentPriority = new AtomicInteger(1);
    }

    public int getCurrentPriority() {
        return currentPriority.get();
    }

    public void incrementPriority() {
        currentPriority.incrementAndGet();
    }

    public void decrementPriority() {
        currentPriority.decrementAndGet();
    }

    @Override
    protected <T> PrioritizedRunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new PrioritizedRunnableFuture<>(runnable, value, currentPriority.get());
    }

    @Override
    protected <T> PrioritizedRunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new PrioritizedRunnableFuture<>(callable, currentPriority.get());
    }

    private static class ComparePriority<T extends PrioritizedRunnableFuture> implements Comparator<T> {

        @Override
        public int compare(final T o1, final T o2) {
            return o1.compareTo(o2);
        }
    }
    private AtomicInteger currentPriority;
}
