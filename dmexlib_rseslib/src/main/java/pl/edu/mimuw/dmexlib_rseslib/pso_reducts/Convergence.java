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
public class Convergence implements IConvergence<Double> {

    public Convergence(int maxGeneration, double minFitness, Inertia inertia) {
        this.maxGeneration = maxGeneration;
        this.minFitness = minFitness;
        this.inertia = inertia;
    }

    @Override
    public boolean isConvergent(int generation, Double best, Double current) {
        inertia.update(generation);
        return generation > maxGeneration || best > minFitness;
    }
    
    private final int maxGeneration;
    private final double minFitness;
    private final Inertia inertia;
}
