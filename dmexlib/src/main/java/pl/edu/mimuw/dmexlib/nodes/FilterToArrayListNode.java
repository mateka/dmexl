/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.ArrayList;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;

/**
 *
 * @author matek
 */
public class FilterToArrayListNode<Type, Filter extends IFilterOperation<Type>> extends FilterNode<Type, Filter, ArrayList<Type>> {

    public FilterToArrayListNode(Algorithm<Iterable<Type>> elements, Algorithm<Filter> filter) {
        super(elements, filter);
    }

    @Override
    public ArrayList<Type> createNewCollection() {
        return new ArrayList<>();
    }
    
}
