/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import pl.edu.mimuw.dmexlib.domain.IDomainPart;
import pl.edu.mimuw.dmexlib.executors.IExecutor;

/**
 * Base interface for algorithm's execution context
 * TODO Make a project for it
 * @author matek
 */
public interface IExecutionContext {
    public IExecutor getExecutor();
    public IDomainPart getDomainPart();
}
