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
public interface IConvergence<ValueType extends Comparable<ValueType>> {

    public boolean isConvergent(int generation, ValueType best, ValueType current);
}
