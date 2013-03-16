/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.Iterator;
import java.util.ListIterator;
import junit.framework.TestCase;
import pl.edu.mimuw.dmexlib.nodes.operations.IGenerateOperation;

/**
 *
 * @author matek
 */
public class TestLazyList extends TestCase {

    public TestLazyList(String testName) {
        super(testName);
    }
    
    public void testBaseConditions() {
        final int size = 17;
        
        LazyList<Integer, GenOp> myList = new LazyList<Integer, GenOp>(size, new GenOp());
        
        assertEquals(myList.size(), size);
        
        ListIterator<Integer> begin = myList.listIterator();
        ListIterator<Integer> end = myList.listIterator(myList.size());
        
        assertFalse(begin.hasPrevious());
        assertTrue(begin.hasNext());
        assertEquals(begin.nextIndex(), 1);
        assertEquals(begin.previousIndex(), -1);
        
        assertFalse(end.hasNext());
        assertTrue(end.hasPrevious());
        assertEquals(end.previousIndex(), myList.size()-1);
        assertEquals(end.nextIndex(), myList.size());
    }

    public void testIndexIteration() {
        LazyList<Integer, GenOp> myList = new LazyList<Integer, GenOp>(10, new GenOp());
        for (int i = 0; i < myList.size(); ++i) {
            assertEquals(i, (int) myList.get(i));
        }
    }

    public void testBaseIteration() {
        LazyList<Integer, GenOp> myList = new LazyList<Integer, GenOp>(50, new GenOp());
        int i = 0;
        for (Iterator<Integer> it = myList.iterator(); it.hasNext();) {
            assertEquals(i++, (int) it.next());
        }
    }

    public void testBackwardsIteration() {
        final int size = 13;
        
        LazyList<Integer, GenOp> myList = new LazyList<Integer, GenOp>(size, new GenOp());
        int i = size;
        for (ListIterator<Integer> it = myList.listIterator(myList.size()); it.hasPrevious();) {
            assertEquals(--i, (int) it.previous());
        }
    }

    public void testSubListsIteration() {
        LazyList<Integer, GenOp> myList = new LazyList<Integer, GenOp>(177, new GenOp());
        int i=0;
        for (Iterator<Integer> it = myList.subList(0, myList.size()/2).iterator(); it.hasNext();) {
            assertEquals(i++, (int) it.next());
        }
        for (Iterator<Integer> it = myList.subList(myList.size()/2, myList.size()).iterator(); it.hasNext();) {
            assertEquals(i++, (int) it.next());
        }
    }

    private static class GenOp implements IGenerateOperation<Integer> {

        @Override
        public Integer invoke(int param) {
            return param;
        }
    }
}
