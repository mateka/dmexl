/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.mimuw.dmexlib_rseslib.data_views;

import java.util.ArrayList;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import rseslib.structure.data.DoubleData;
import rseslib.structure.data.NumberedDoubleDataObject;
import rseslib.structure.table.ArrayListDoubleDataTable;
import rseslib.structure.table.DoubleDataTable;

/**
 *
 * @author Mateusz
 */
public class DoubleDataTableViewTest extends TestCase {
    public static DoubleDataTableView createEmptyTable() {
        HeaderView head = HeaderViewTest.createHeader();
        DoubleDataTable t = new ArrayListDoubleDataTable(head.getBaseHeader());
        return new DoubleDataTableView(t, head);
    }
    
    public DoubleDataTableViewTest(String testName) {
        super(testName);
    }

    /**
     * Test of noOfObjects method, of class DoubleDataTableView.
     */
    public void testNoOfObjects() {
        DoubleDataTableView instance = createEmptyTable();
        
        assertEquals(0, instance.noOfObjects());
        
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 0));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 1));
        
        assertEquals(2, instance.noOfObjects());
    }

    /**
     * Test of add method, of class DoubleDataTableView.
     */
    public void testAdd() {
        DoubleDataTableView instance = createEmptyTable();
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 0));
    }

    /**
     * Test of remove method, of class DoubleDataTableView.
     */
    public void testRemove() {
        DoubleDataTableView instance = createEmptyTable();
        
        assertEquals(0, instance.noOfObjects());
        
        DoubleData obj = new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 0);
        instance.add(obj);
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 1));
        assertEquals(2, instance.noOfObjects());
        
        assertEquals(true, instance.remove(obj));
        assertEquals(false, instance.remove(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 3)));
    }

    /**
     * Test of getDataObjects method, of class DoubleDataTableView.
     */
    public void testGetDataObjects() {
        DoubleDataTableView instance = createEmptyTable();
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 0));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 1));
        
        ArrayList<DoubleData> result = instance.getDataObjects();
        assertNotNull(result);
    }

    /**
     * Test of randomSplit method, of class DoubleDataTableView.
     */
    public void testRandomSplit() {
        DoubleDataTableView instance = createEmptyTable();
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 0));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 1));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 2));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 3));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 4));
        
        ArrayList[] result = instance.randomSplit(1, 2);
        assertNotNull(result);
    }

    /**
     * Test of randomPartition method, of class DoubleDataTableView.
     */
    public void testRandomPartition() {
        DoubleDataTableView instance = createEmptyTable();
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 0));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 1));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 2));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 3));
        instance.add(new NumberedDoubleDataObject(HeaderViewTest.createHeader(), 4));
        
        ArrayList[] result = instance.randomPartition(3);
        assertNotNull(result);
    }

    /**
     * Test of store method, of class DoubleDataTableView.
     */
    public void testStore() throws Exception {
        try {
            DoubleDataTableView instance = createEmptyTable();
            instance.store(null, null);
            fail("Should throw");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of storeArff method, of class DoubleDataTableView.
     */
    public void testStoreArff() throws Exception {
        try {
            DoubleDataTableView instance = createEmptyTable();
            instance.storeArff("name", null, null);
            fail("Should throw");
        } catch (UnsupportedOperationException e) {
        }
    }
    
}
