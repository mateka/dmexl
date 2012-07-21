/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations;

/**
 *
 * @author matek
 */
public interface IFilterOperation<Type> {
    public boolean invoke(Type argument);
    
}
