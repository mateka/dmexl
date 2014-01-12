/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations.pso;

/**
 *
 * @author Mateusz
 */
public interface ICostFunction<ValueType extends Comparable<ValueType>, Position> {

    public ValueType evaluate(Position p);
}
