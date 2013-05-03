/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes;

import java.util.List;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.GenerateNode;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;

/**
 *
 * @author matek
 */
public interface IGenerateExecutor {

    public <E, O extends IGenerateOperation<E>> List<E> execute(GenerateNode<E, O> algo, IExecutionContext ctx) throws Exception;
}
