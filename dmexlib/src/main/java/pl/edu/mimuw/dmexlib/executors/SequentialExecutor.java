/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;

/**
 * Simple, sequential executor
 *
 * @author matek
 */
public class SequentialExecutor implements IExecutor {

    @Override
    public <Result> ResultType<Result> execute(Algorithm<Result> algo) {
        SimpleSequentialExecutionContext ctx = new SimpleSequentialExecutionContext(this);
        return execute(algo, ctx);
    }

    @Override
    public <Result> ResultType<Result> execute(Algorithm<Result> algo, IExecutionContext ctx) {
        return algo.sequentialExecute(ctx);
    }
}
