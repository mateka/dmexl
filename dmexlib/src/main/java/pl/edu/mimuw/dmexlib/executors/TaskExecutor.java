/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.executors;

/**
 *
 * @author matek
 */
public class TaskExecutor extends CustomizableTaskExecutor {

    public TaskExecutor(int nThreads) {
        super(new SimpleTaskManager(nThreads));
    }

}
