/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes.operations;

import pl.edu.mimuw.dmexlib.ResultType;

/**
 * Interface for operation invoked by accumulate algorithms
 *
 * @author matek
 */
public interface IAccumulateOperation<Result, Argument> {
    public ResultType<Result> invoke(Result left, Argument right);
}
