/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.tasks;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IPSOExecutor;
import pl.edu.mimuw.dmexlib.nodes.PSONode;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;

/**
 *
 * @author Mateusz
 */
public class PSOExecutor implements IPSOExecutor {

    public PSOExecutor(IExecutorTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> Particle execute(PSONode<VT, P, Particle, Cost, Convergence> algo, IExecutionContext ctx) throws Exception {
        List<Future<TaskResult<VT, P, Particle>>> tasks = null;
        try {
            // Schedule calculations for subnodes
            Future<List<Particle>> a = taskManager.createAlgorithmTask(algo.getA(), ctx);
            Future<Cost> b = taskManager.createAlgorithmTask(algo.getB(), ctx);
            Future<Convergence> c = taskManager.createAlgorithmTask(algo.getC(), ctx);

            // Get results from subnodes
            final List<Particle> particles = a.get();
            final Cost cost = b.get();
            final Convergence covergence = c.get();

            final int nParticles = particles.size();

            // Initialize tasks list with calculating cost for each particle
            CompletionService<TaskResult<VT, P, Particle>> ecs = taskManager.getCompletionService();
            tasks = new LinkedList<Future<TaskResult<VT, P, Particle>>>();
            for (Iterator<Particle> parts = particles.iterator(); parts.hasNext();) {
                final Particle p = parts.next();
                addTask(tasks, ecs, p, cost);   // Add task for updated particle
            }

            boolean stop = false;
            Particle best = null;
            // Start from 1, because we calculated first generation above:
            for (int generation = 1; !stop; ++generation) {
                for (int partI = 0; partI < nParticles; ++partI) {
                    // Update calculated fitness
                    TaskResult<VT, P, Particle> tr = ecs.take().get();
                    final Particle p = tr.getPart();
                    p.setFitness(tr.getFitness());
                    
                    addTask(tasks, ecs, p, cost);   // Add task for updated particle
                    // If there was best particle, then check convergence.
                    // Update best foound particle
                    if (null != best) {
                        stop = covergence.isConvergent(generation, best.getFitness(), p.getFitness());
                        // Update best particle
                        if (best.getFitness().compareTo(p.getFitness()) < 0) {
                            best = p;
                        }
                        // Stop when process is convergent
                        if (stop) {
                            break;
                        }
                    } else {
                        best = p;
                    }

                    p.update(best.getPosition(), ctx.getRandom());
                }
            }

            return best;
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

    private <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>>
            void addTask(List<Future<TaskResult<VT, P, Particle>>> tasks, CompletionService<TaskResult<VT, P, Particle>> ecs, final Particle p, final Cost cost) {
        tasks.add(ecs.submit(new Callable<TaskResult<VT, P, Particle>>() {
            @Override
            public TaskResult<VT, P, Particle> call() throws Exception {
                return new TaskResult<VT, P, Particle>(p, cost.evaluate(p.getPosition()));
            }
        }));
    }

    private class TaskResult<VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>> {

        public TaskResult(Particle part, VT fitness) {
            this.fitness = fitness;
            this.part = part;
        }

        public VT getFitness() {
            return fitness;
        }

        public Particle getPart() {
            return part;
        }

        private final VT fitness;
        private final Particle part;
    }

    private final IExecutorTaskManager taskManager;
}
