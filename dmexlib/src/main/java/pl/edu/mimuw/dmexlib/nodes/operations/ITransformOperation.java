/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations;

/**
 * Interface for operation invoked by transform algorithms
 *
 * @author matek
 */
public interface ITransformOperation<Result, Argument> {

    public Result invoke(Argument arg);
}
