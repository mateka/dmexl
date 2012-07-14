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
public abstract class BinaryNode <Result, A1, A2> implements Algorithm<Result> {

    public BinaryNode(A1 left, A2 right) {
        this.left = left;
        this.right = right;
    }

    public final A1 getLeft() {
        return left;
    }

    public final A2 getRight() {
        return right;
    }
        
    private final A1 left;
    private final A2 right;
}
