/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.data_views;

import junit.framework.TestCase;
import rseslib.structure.data.DoubleData;
import rseslib.structure.data.DoubleDataObject;

/**
 *
 * @author Mateusz
 */
public class DoubleDataViewTest extends TestCase {

    public static DoubleDataView createRow() {
        HeaderView head = HeaderViewTest.createHeader();
        DoubleData dd = new DoubleDataObject(head.getBaseHeader());
        return new DoubleDataView(dd, head);
    }

    public DoubleDataViewTest(String testName) {
        super(testName);
    }

    /**
     * Test of set method, of class DoubleDataView.
     */
    public void testSet() {
        DoubleDataView instance = createRow();
        instance.set(1, 1.0);
    }

    /**
     * Test of get method, of class DoubleDataView.
     */
    public void testGet() {
        DoubleDataView instance = createRow();
        instance.set(1, 1.0);

        assertEquals(1.0, instance.get(1), 0.0);
    }

    /**
     * Test of store method, of class DoubleDataView.
     */
    public void testStore() throws Exception {
        try {
            DoubleDataView instance = createRow();
            instance.store(null);
            fail("Should throw");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of storeArff method, of class DoubleDataView.
     */
    public void testStoreArff() throws Exception {
        try {
            DoubleDataView instance = createRow();
            instance.storeArff(null);
            fail("Should throw");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of equals method, of class DoubleDataView.
     */
    public void testEquals() {
        DoubleDataView obj = createRow();
        DoubleDataView instance = createRow();

        assertEquals(true, instance.equals(obj));
        System.out.println("equals");
        obj = (DoubleDataView) instance.clone();
        assertEquals(true, instance.equals(obj));
    }

}
