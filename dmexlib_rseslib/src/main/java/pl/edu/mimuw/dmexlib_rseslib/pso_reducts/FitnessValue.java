/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

/**
 *
 * @author Mateusz
 */
public class FitnessValue implements Comparable<FitnessValue> {

    public FitnessValue(double fitness, double accuracy) {
        this.fitness = fitness;
        this.accuracy = accuracy;
    }

    public double getFitness() {
        return fitness;
    }

    public double getAccuracy() {
        return accuracy;
    }
    
    
    @Override
    public int compareTo(FitnessValue o) {
        return Double.compare(getFitness(), o.getFitness());
    }
    
    private final double fitness;
    private final double accuracy; 

    
}
