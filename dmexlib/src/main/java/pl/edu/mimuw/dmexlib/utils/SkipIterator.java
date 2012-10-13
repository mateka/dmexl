/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.utils;

import java.util.Iterator;

/**
 *
 * @author matek
 */
public class SkipIterator<E> implements Iterator<E> {

    public SkipIterator(Iterator<E> base, int start, int skip) {
        if (skip < start) {
            throw new IllegalArgumentException("skip < start");
        }

        this.current = base;
        this.skip = skip-1;

        for (int i = 0; i < start && current.hasNext(); ++i) {
            current.next();
        }
    }
    // TODO
//    public SkipIterator(SkipIterator<E> base, int start, int skip) {
//        if (skip < start) {
//            throw new IllegalArgumentException("skip < start");
//        }
//
//        this.current = base.current;
//        this.skip = skip-1 + base.skip;
//
//        for (int i = 0; i < start && current.hasNext(); ++i) {
//            current.next();
//        }
//    }

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
