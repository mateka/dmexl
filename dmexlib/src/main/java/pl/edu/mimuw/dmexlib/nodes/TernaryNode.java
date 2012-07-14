/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import pl.edu.mimuw.dmexlib.Algorithm;

/**
 * Helper class for implementing algorithm node with three params
 *
 * @author matek
 */
public abstract class TernaryNode<Result, A, B, C> implements Algorithm<Result> {

    public TernaryNode(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
    private final A a;
    private final B b;
    private final C c;
}
