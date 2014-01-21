/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.data_views;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import rseslib.structure.attribute.ArrayHeader;
import rseslib.structure.attribute.Attribute;
import rseslib.structure.attribute.Attribute.Type;
import rseslib.structure.attribute.Header;
import rseslib.structure.attribute.NominalAttribute;

/**
 *
 * @author Mateusz
 */
public class HeaderViewTest extends TestCase {

    public static HeaderView createHeader() {
        final int attrNo = 5;
        Attribute[] attributes = new Attribute[attrNo];
        for (int i = 0; i < attrNo-1; ++i) {
            attributes[i] = new NominalAttribute(Type.conditional, Integer.toString(i));
        }
        attributes[attrNo-1] = new NominalAttribute(Type.decision, Integer.toString(attrNo-1));
        Header hdr = new ArrayHeader(attributes, "?");

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0, 0);
        map.put(1, 2);
        map.put(2, 4);

        return new HeaderView(hdr, map);
    }

    public HeaderViewTest(String testName) {
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
     * Test of map method, of class HeaderView.
     */
    public void testMap() {
        HeaderView instance = createHeader();

        assertEquals(0, instance.map(0));
        assertEquals(2, instance.map(1));
        assertEquals(4, instance.map(2));
    }

    /**
     * Test of noOfAttr method, of class HeaderView.
     */
    public void testNoOfAttr() {
        HeaderView instance = createHeader();

        assertEquals(3, instance.noOfAttr());
    }

    /**
     * Test of attribute method, of class HeaderView.
     */
    public void testAttribute() {
        HeaderView instance = createHeader();

        Attribute result = instance.attribute(1);
        assertEquals(true, result.isNominal());
        assertEquals(Integer.toString(2), result.name());
    }

    /**
     * Test of name method, of class HeaderView.
     */
    public void testName() {
        HeaderView instance = createHeader();

        assertEquals(Integer.toString(4), instance.name(2));
    }

    /**
     * Test of isNumeric method, of class HeaderView.
     */
    public void testIsNumeric() {
        HeaderView instance = createHeader();

        assertEquals(false, instance.isNumeric(0));
    }

    /**
     * Test of isNominal method, of class HeaderView.
     */
    public void testIsNominal() {
        HeaderView instance = createHeader();

        assertEquals(true, instance.isNominal(0));
    }

    /**
     * Test of missing method, of class HeaderView.
     */
    public void testMissing() {
        HeaderView instance = createHeader();

        assertEquals("?", instance.missing());
    }

    /**
     * Test of store method, of class HeaderView.
     */
    public void testStore() throws Exception {
        try {
            HeaderView instance = createHeader();
            instance.store(null);
            fail("Should throw");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test of storeArff method, of class HeaderView.
     */
    public void testStoreArff() throws Exception {
        try {
            HeaderView instance = createHeader();
            instance.storeArff("name", null);
            fail("Should throw");
        } catch (UnsupportedOperationException e) {
        }
    }

}
