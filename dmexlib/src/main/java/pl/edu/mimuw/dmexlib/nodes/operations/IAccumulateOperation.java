/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations;

/**
 * Interface for operation invoked by accumulate algorithms
 *
 * @author matek
 */
public interface IAccumulateOperation<Result, Argument> {
    public Result invoke(Argument arg);
    public Result invoke(Result left, Result right);
}
