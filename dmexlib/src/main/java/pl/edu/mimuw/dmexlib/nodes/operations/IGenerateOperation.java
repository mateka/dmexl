/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations;

/**
 *
 * @author matek
 */
public interface IGenerateOperation<Type> {
    public Type invoke(final int param);
}
