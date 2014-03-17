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
public interface Inertia {

    public float get();

    public void update(int iteration);
}
