/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

import java.util.Random;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;

/**
 *
 * @author Mateusz
 */
public class Particle implements IParticle<Double, Position> {

    public Particle(Position position, int velocity, Inertia inertia) {
        this.position = position;
        this.fitness = 0.0;
        this.velocity = velocity;
        this.inertia = inertia;

        this.best = new Position(position);
        this.bestFitness = 0.0;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Double getFitness() {
        return fitness;
    }

    @Override
    public void update(Position globalBest, Random rnd) {
        Position prev = new Position(getPosition()); // store current position

        position.update(globalBest, velocity, rnd); // move
        velocity = calcVelocity(globalBest, rnd); // update velocity

        // Update particle's best
        if (bestFitness < fitness) {
            best = prev;
            bestFitness = fitness;
        }
    }

    @Override
    public void setFitness(Double newFitness) {
        fitness = newFitness;
    }

    private int calcVelocity(Position globalBest, Random rnd) {
        final float minV = 1.0f;
        final float maxV = position.dimensions() / 3.0f;

        final float c1 = 2.0f;
        final float c2 = 2.0f;

        float v = inertia.get() * velocity;
        v += c1 * rnd.nextDouble() * position.distanceTo(globalBest);
        v += c2 * rnd.nextDouble() * position.distanceTo(best);

        if (v < minV) {
            v = minV;
        }
        if (v > maxV) {
            v = maxV;
        }

        return Math.round(v);
    }

    // Current
    private final Position position;
    private double fitness;
    private int velocity;
    private final Inertia inertia;

    // Best
    private Position best;
    private double bestFitness;
}
