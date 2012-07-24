/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

/**
 * Tests for SkipIterator
 *
 * @author matek
 */
public class TestSkipIterator extends TestCase {
    
    public TestSkipIterator(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
//    public static Test suite()
//    {
//        return new TestSuite( AppTest.class );
//    }
    public void testCreate() {
        final List<Integer> l1 = generateList(1);
        SkipIterator<Integer> skipi1 = new SkipIterator<>(l1, 0, 0);
        
        try {
            final List<Integer> l2 = generateList(1);
            SkipIterator<Integer> skipi2 = new SkipIterator<>(l2, 2, 1);
            fail("Constructor should throw for skip < start");
        } catch (IllegalArgumentException ignore) {
        }
    }
    
    public void testHasNext() {
        // SkipIterator starting after collection should not have next
        final List<Integer> l1 = generateList(2);
        SkipIterator<Integer> skipi1 = new SkipIterator<>(l1, 3, 3);
        assertFalse(skipi1.hasNext());

        // Skip iterator in collection should have next
        final List<Integer> l2 = generateList(4);
        SkipIterator<Integer> skipi2 = new SkipIterator<>(l2, 2, 3);
        assertTrue(skipi2.hasNext());
        skipi2.next();
        assertFalse(skipi2.hasNext());
    }
    
    public void testNext() {
        // Test iteration over collection
        int current = 3;
        final int skip = 3;
        
        final List<Integer> list = generateList(15);
        SkipIterator<Integer> skipi = new SkipIterator<>(list, current-1, skip);
        
        while (skipi.hasNext()) {
            assertEquals(current, (int) skipi.next());
            current += skip;
        }
    }
    
    public void testLooping() {
        // Main idea of SkipIterator is to split iteration into n parts.
        // So after doing all parts, whole collection should have been seen.
        final List<Integer> list0 = generateList(25);
        final int parts = 5;
        List<SkipIterator<Integer>> skipis = new ArrayList<>(parts);
        for (int i = 0; i < parts; ++i) {
            skipis.add(new SkipIterator<>(list0, i, parts));
        }
        
        List<Integer> result = new ArrayList<>(25);
        for (Iterator<SkipIterator<Integer>> outer = skipis.iterator(); outer.hasNext();) {
            for (SkipIterator<Integer> inner = outer.next(); inner.hasNext();) {
                result.add(inner.next());
            }
        }
        Collections.sort(result);
        
        assertEquals(list0, result);
    }
    
    public void testRemove() {
        List<Float> list = new ArrayList<>();
        SkipIterator<Float> skipi = new SkipIterator<>(list, 0, 1);
        try {
            skipi.remove();
            fail("SkipIterator.remove should throw");
        } catch (UnsupportedOperationException ignore) {
        }
    }

    // Helper method for creating list of ints
    private List<Integer> generateList(int length) {
        List<Integer> result = new ArrayList<>(length);
        for (int i = 1; i <= length; ++i) {
            result.add(i);
        }
        return result;
    }
}
