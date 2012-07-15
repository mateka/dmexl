/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.Iterator;

/**
 *
 * @author matek
 */
public class SkipIterator<E> implements Iterator<E> {

    public SkipIterator(Iterable<E> base, int start, int skip) {
        this.current = base.iterator();
        this.skip = skip;

        for (int i = 0; i < start && current.hasNext(); ++i) {
            current.next();
        }
    }

    @Override
    public boolean hasNext() {
        return current.hasNext();
    }

    @Override
    public E next() {
        E result = current.next();

        for (int i = 0; i < skip && current.hasNext(); ++i) {
            current.next();
        }
        
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    private Iterator<E> current;
    private int skip;
}
