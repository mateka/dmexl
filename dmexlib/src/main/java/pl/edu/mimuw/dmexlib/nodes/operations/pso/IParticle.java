/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations.pso;

import java.util.Random;

/**
 *
 * @author Mateusz
 */
public interface IParticle<ValueType extends Comparable<ValueType>, Position> {

    public Position getPosition();

    public ValueType getFitness();

    public void update(Position globalBest, Random rnd);

    public void setFitness(ValueType newFitness);

}
