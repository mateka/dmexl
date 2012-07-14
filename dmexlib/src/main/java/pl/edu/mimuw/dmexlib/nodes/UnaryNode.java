/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import pl.edu.mimuw.dmexlib.Algorithm;

/**
 *
 * @author matek
 */
public abstract class UnaryNode<Result, A1> implements Algorithm<Result> {

    public UnaryNode(A1 argument) {
        this.argument = argument;
    }

    public final A1 getArgument() {
        return argument;
    }
    
    private final A1 argument;
}
