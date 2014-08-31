/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;

/**
 *
 * @author Mateusz
 */
public class Convergence implements IConvergence<FitnessValue> {

    public Convergence(int maxGeneration, double minFitness, Inertia inertia) {
        this.maxGeneration = maxGeneration;
        this.minFitness = minFitness;
        this.inertia = inertia;
    }

    public int getReachedGeneration() {
        return reachedGeneration;
    }

    @Override
    public boolean isConvergent(int generation, FitnessValue best, FitnessValue current) {
        inertia.update(generation);
        reachedGeneration = Math.max(generation, reachedGeneration);
        return generation > maxGeneration || best.getFitness() > minFitness;
    }
    
    private final int maxGeneration;
    private final double minFitness;
    private final Inertia inertia;
    private int reachedGeneration = Integer.MIN_VALUE;
}
