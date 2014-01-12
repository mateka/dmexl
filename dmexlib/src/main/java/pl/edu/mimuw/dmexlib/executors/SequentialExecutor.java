/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.AccumulateExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.FilterExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.GenerateExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.PSOExecutor;
import pl.edu.mimuw.dmexlib.executors.single_nodes.dummy.TransformExecutor;

/**
 * Simple, sequential executor
 *
 * @author matek
 */
public class SequentialExecutor extends CustomizableTaskExecutor {

    public SequentialExecutor() {
        super(null, new AccumulateExecutor(), new TransformExecutor(), new FilterExecutor(), new GenerateExecutor(), new PSOExecutor());
    }

}
