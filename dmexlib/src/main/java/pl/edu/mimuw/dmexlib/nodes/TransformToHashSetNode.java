/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.HashSet;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TransformToHashSetNode <Result, Element, Operation extends ITransformOperation<Result, Element>>
extends TransformNode<Result, Element, Operation, HashSet<Result>>{

    public TransformToHashSetNode(Algorithm<Iterable<Element>> left, Algorithm<Operation> right) {
        super(left, right);
    }

    @Override
    protected HashSet<Result> createNewCollection() {
        return new HashSet<>();
    }
    
    
}
