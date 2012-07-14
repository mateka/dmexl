/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;

/**
 * Interface for one algorithm. It allows to execute it: sequentially,
 * on multi-core CPU and on GPU with use of given context (@see IExecutionContext).
 * @author matek
 */
public interface Algorithm<Result> {
    // TODO Methods should be public or package private?
    public ResultType<Result> sequentialExecute(IExecutionContext ctx);
    public ResultType<Result> multiCPUExecute(IExecutionContext ctx);
    public ResultType<Result> GPUExecute(IExecutionContext ctx);
}
