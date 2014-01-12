/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors.single_nodes.tasks;

import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.executors.single_nodes.IPSOExecutor;
import pl.edu.mimuw.dmexlib.nodes.PSONode;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;

/**
 *
 * @author Mateusz
 */
public class PSOExecutor implements IPSOExecutor {

    public PSOExecutor(IExecutorTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public <VT extends Comparable<VT>, P, Particle extends IParticle<VT, P>, Cost extends ICostFunction<VT, P>, Convergence extends IConvergence<VT>> Particle execute(PSONode<VT, P, Particle, Cost, Convergence> algo, IExecutionContext ctx) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private final IExecutorTaskManager taskManager;
}
