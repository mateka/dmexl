/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.nodes;

import java.util.Collection;
import java.util.HashSet;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;

/**
 *
 * @author matek
 */
public class FilterToHashSetNode<Type, Filter extends IFilterOperation<Type>> extends FilterNode<Type, Filter> {

    public FilterToHashSetNode(Algorithm<Iterable<Type>> elements, Algorithm<Filter> filter) {
        super(elements, filter);
    }

    @Override
    protected Collection<Type> createNewCollection() {
        return new HashSet<>();
    }
    
}
