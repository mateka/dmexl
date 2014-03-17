/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

/**
 *
 * @author Mateusz
 */
public class PSORSFSInertia implements Inertia {

    public PSORSFSInertia(float value, int maxIteration) {
        this.value = value;
        this.maxIteration = maxIteration;
    }

    @Override
    public float get() {
        return value;
    }

    @Override
    public void update(int iteration) {
        final float c = 0.4f;

        value = (value - c) * ((float) maxIteration - iteration) / maxIteration + c;
    }

    private float value;
    private final int maxIteration;
}
