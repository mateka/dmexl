/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.data_views;

import java.io.BufferedWriter;
import java.io.IOException;
import rseslib.structure.attribute.Header;
import rseslib.structure.data.DoubleData;

/**
 *
 * @author Mateusz
 */
public class DoubleDataView implements DoubleData {

    public DoubleDataView(DoubleData object, HeaderView header) {
        this.object = object;
        this.header = header;
    }

    @Override
    public void set(int atrNo, double value) {
        this.object.set(this.header.map(atrNo), value);
    }

    @Override
    public double get(int atrNo) {
        return this.object.get(this.header.map(atrNo));
    }

    @Override
    public void store(BufferedWriter output) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void storeArff(BufferedWriter output) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Header attributes() {
        return header;
    }

    @Override
    public Object clone() {
        return new DoubleDataView((DoubleData) object.clone(), header);
    }

    public boolean equals(DoubleDataView obj) {
        if (obj.header.noOfAttr() != header.noOfAttr()) {
            return false;
        }

        for (int i = 0; i < header.noOfAttr(); ++i) {
            if (get(i) != obj.get(i)) {
                return false;
            }
        }
        return true;
    }

    protected DoubleData getObject() {
        return object;
    }

    private final DoubleData object;
    private final HeaderView header;
}
