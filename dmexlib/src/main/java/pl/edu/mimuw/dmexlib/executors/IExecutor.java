/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;

/**
 * Interface for all executors.
 * @author matek
 */
public interface IExecutor {
    public <Result> ResultType<Result> execute(Algorithm<Result> algo);
    public <Result> ResultType<Result> execute(Algorithm<Result> algo, IExecutionContext ctx);
}
