/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.execution_contexts;

import pl.edu.mimuw.dmexlib.domain.DomainPart;
import pl.edu.mimuw.dmexlib.domain.FullDomain;
import pl.edu.mimuw.dmexlib.domain.IDomainPart;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.SequentialExecutor;

/**
 *
 * @author matek
 */
public class SimpleSequentialExecutionContext implements IExecutionContext {

    public SimpleSequentialExecutionContext(SequentialExecutor executor) {
        this.executor = executor;
        this.domainPart = new FullDomain();
    }

    public SimpleSequentialExecutionContext(SequentialExecutor executor, DomainPart domainPart) {
        this.executor = executor;
        this.domainPart = domainPart;
    }

    @Override
    public IExecutor getExecutor() {
        return executor;
    }

    @Override
    public IDomainPart getDomainPart() {
        return domainPart;
    }
    private SequentialExecutor executor;
    private IDomainPart domainPart;
}
