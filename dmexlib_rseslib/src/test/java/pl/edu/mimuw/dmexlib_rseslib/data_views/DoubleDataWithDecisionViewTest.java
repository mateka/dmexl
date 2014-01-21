/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.mimuw.dmexlib_rseslib.data_views;

import junit.framework.TestCase;
import rseslib.structure.data.DoubleDataWithDecision;
import rseslib.structure.data.NumberedDoubleDataObject;

/**
 *
 * @author Mateusz
 */
public class DoubleDataWithDecisionViewTest extends TestCase {
    public static DoubleDataWithDecisionView createRow() {
        HeaderView head = HeaderViewTest.createHeader();
        DoubleDataWithDecision dd = new NumberedDoubleDataObject(head.getBaseHeader(), 0);
        return new DoubleDataWithDecisionView(dd, head);
    }
    
    public DoubleDataWithDecisionViewTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setDecision method, of class DoubleDataWithDecisionView.
     */
    public void testSetDecision() {
        DoubleDataWithDecisionView instance = createRow();
        instance.setDecision(2.5);
    }

    /**
     * Test of getDecision method, of class DoubleDataWithDecisionView.
     */
    public void testGetDecision() {
        DoubleDataWithDecisionView instance = createRow();
        instance.setDecision(2.5);
        
        assertEquals(2.5, instance.getDecision(), 0.0);
    }
    
}
