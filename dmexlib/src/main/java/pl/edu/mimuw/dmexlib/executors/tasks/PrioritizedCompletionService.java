/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.tasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * copied from java
 */
public class PrioritizedCompletionService<V> implements CompletionService<V> {

    public PrioritizedCompletionService(PriorityExecutorService executor) {
        this.executor = executor;
        this.completionQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public Future<V> submit(Callable<V> task) {
        PrioritizedRunnableFuture<V> f = newTaskFor(task);
        executor.execute(f);
        return f;
    }

    @Override
    public Future<V> submit(Runnable task, V result) {
        PrioritizedRunnableFuture<V> f = newTaskFor(task, result);
        executor.execute(f);
        return f;
    }

    @Override
    public Future<V> take() throws InterruptedException {
        return completionQueue.take();
    }

    @Override
    public Future<V> poll() {
        return completionQueue.poll();
    }

    @Override
    public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return completionQueue.poll(timeout, unit);
    }

    private QueueingFuture newTaskFor(Callable<V> task) {
        return new QueueingFuture(task, executor.getCurrentPriority());
    }

    private QueueingFuture newTaskFor(Runnable task, V result) {
        return new QueueingFuture(task, result, executor.getCurrentPriority());
    }

    private class QueueingFuture extends PrioritizedRunnableFuture<V> {

        public QueueingFuture(Callable<V> callable, int priority) {
            super(callable, priority);
        }

        public QueueingFuture(Runnable runnable, V result, int priority) {
            super(runnable, result, priority);
        }


        @Override
        protected void done() {
            completionQueue.add(this);
        }
    }
    private PriorityExecutorService executor;
    private BlockingQueue<PrioritizedRunnableFuture<V>> completionQueue;
}