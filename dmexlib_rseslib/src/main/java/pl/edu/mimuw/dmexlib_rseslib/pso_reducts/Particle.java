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
public class Particle implements IParticle<FitnessValue, Position> {

    public Particle(Position position, int velocity, Inertia inertia) {
        this.position = position;
        this.fitness = new FitnessValue(0.0, 0.0);
        this.velocity = velocity;
        this.inertia = inertia;

        this.best = new Position(position);
        this.bestFitness = new FitnessValue(0.0, 0.0);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public FitnessValue getFitness() {
        return fitness;
    }

    @Override
    public void update(Position globalBest, Random rnd) {
        Position prev = new Position(getPosition()); // store current position

        position.update(globalBest, velocity, rnd); // move
        velocity = calcVelocity(globalBest, rnd); // update velocity

        // Update particle's best
        if (bestFitness.getFitness() < fitness.getFitness()) {
            best = prev;
            bestFitness = fitness;
        }
    }

    @Override
    public void setFitness(FitnessValue newFitness) {
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
    private FitnessValue fitness;
    private int velocity;
    private final Inertia inertia;

    // Best
    private Position best;
    private FitnessValue bestFitness;
}
