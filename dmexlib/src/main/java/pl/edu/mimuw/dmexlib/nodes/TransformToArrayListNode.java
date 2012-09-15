/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.ArrayList;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class TransformToArrayListNode<Result, Element, Operation extends ITransformOperation<Result, Element>>
extends TransformNode<Result, Element, Operation, ArrayList<Result>>{

    public TransformToArrayListNode(Algorithm<Iterable<Element>> left, Algorithm<Operation> right) {
        super(left, right);
    }

    @Override
    public ArrayList<Result> createNewCollection() {
        return new ArrayList<>();
    }
    
}
