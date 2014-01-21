/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.data_views;

import rseslib.structure.data.DoubleDataWithDecision;

/**
 *
 * @author Mateusz
 */
public class DoubleDataWithDecisionView extends DoubleDataView implements DoubleDataWithDecision {

    public DoubleDataWithDecisionView(DoubleDataWithDecision object, HeaderView header) {
        super(object, header);
    }

    @Override
    public void setDecision(double decVal) {
        ((DoubleDataWithDecision) getObject()).setDecision(decVal);
    }

    @Override
    public double getDecision() {
        return ((DoubleDataWithDecision) getObject()).getDecision();
    }
}
