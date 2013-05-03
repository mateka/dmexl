/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes;

import java.util.List;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.FilterNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;

/**
 *
 * @author matek
 */
public interface IFilterExecutor {

    public <T, F extends IFilterOperation<T>> List<T> execute(FilterNode<T, F> algo, IExecutionContext ctx) throws Exception;
}
