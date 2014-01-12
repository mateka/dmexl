/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.Iterator;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;

/**
 *
 * @author Mateusz
 */
public class PSONode<ValueType extends Comparable<ValueType>, Position, Particle extends IParticle<ValueType, Position>, CostFunction extends ICostFunction<ValueType, Position>, Convergence extends IConvergence<ValueType>>
        extends TernaryNode<Particle, Algorithm<List<Particle>>, Algorithm<CostFunction>, Algorithm<Convergence>> {

    public PSONode(Algorithm<List<Particle>> parts, Algorithm<CostFunction> cost, Algorithm<Convergence> cov) {
        super(parts, cost, cov);
    }

    @Override
    public Particle execute(IExecutionContext ctx) throws Exception {
        // Calculate algorithms in subnodes
        List<Particle> particles = ctx.getExecutor().execute(getA(), ctx);
        ICostFunction<ValueType, Position> cost = ctx.getExecutor().execute(getB(), ctx);
        IConvergence<ValueType> covergence = ctx.getExecutor().execute(getC(), ctx);

        boolean stop = false;
        Particle best = null;
        for (int generation = 0; !stop; ++generation) {
            Iterator<Particle> parts = particles.iterator();
            while (parts.hasNext()) {
                // Calculate fitness for one particle
                Particle p = parts.next();
                p.setFitness(cost.evaluate(p.getPosition()));
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
    }

    @Override
    public Particle accept(IExecutionContext ctx) throws Exception {
        return ctx.getExecutor().execute(this, ctx);
    }

}
