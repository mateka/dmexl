/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.data_views;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import rseslib.structure.attribute.Attribute;
import rseslib.structure.attribute.Header;
import rseslib.structure.attribute.NominalAttribute;

/**
 *
 * @author Mateusz
 */
public class HeaderView implements Header {

    public HeaderView(Header header, Map<Integer, Integer> attribsMapping) {
        this.header = header;
        this.attribsMapping = attribsMapping;

        // Add decision to mapping
        if (!this.attribsMapping.containsValue(header.decision())) {
            decision = attribsMapping.size();
            this.attribsMapping.put(decision, header.decision());
        }
        else { // Find decision
            decision = header.decision();
            for(Map.Entry<Integer,Integer> e : attribsMapping.entrySet()) {
                if(e.getValue() == decision) {
                    decision = e.getKey();
                    break;
                }
            }
        }
    }

    public Header getBaseHeader() {
        return header;
    }

    public int map(int attrInd) {
        return attribsMapping.get(attrInd);
    }
    
    @Override
    public int noOfAttr() {
        return attribsMapping.size();
    }

    @Override
    public Attribute attribute(int attrInd) {
        return header.attribute(map(attrInd));
    }

    @Override
    public String name(int attrInd) {
        return header.name(map(attrInd));
    }

    @Override
    public boolean isInterpretable(int attrInd) {
        return header.isInterpretable(map(attrInd));
    }

    @Override
    public boolean isText(int attrInd) {
        return header.isText(map(attrInd));
    }

    @Override
    public boolean isNumeric(int attrInd) {
        return header.isNumeric(map(attrInd));
    }

    @Override
    public boolean isNominal(int attrInd) {
        return header.isNominal(map(attrInd));
    }

    @Override
    public boolean isMissing(String value) {
        return header.isMissing(value);
    }

    @Override
    public String missing() {
        return header.missing();
    }

    @Override
    public boolean isConditional(int attrInd) {
        return header.isConditional(map(attrInd));
    }

    @Override
    public boolean isDecision(int attrInd) {
        return header.isDecision(map(attrInd));
    }

    @Override
    public int decision() {
        return decision;
    }

    @Override
    public NominalAttribute nominalDecisionAttribute() {
        return header.nominalDecisionAttribute();
    }

    @Override
    public void store(BufferedWriter output) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void storeArff(String name, BufferedWriter output) throws IOException {
        throw new UnsupportedOperationException("Not supported.");
    }

    private Header header;
    private Map<Integer, Integer> attribsMapping;
    int decision;
}
