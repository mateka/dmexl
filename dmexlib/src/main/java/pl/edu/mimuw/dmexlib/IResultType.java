/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import java.util.concurrent.Future;

/**
 *
 * @author matek
 */
public abstract class IResultType<Result> implements Future<Result> {

    public IResultType(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    private boolean ok;
}
