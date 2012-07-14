/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

/**
 *
 * @author matek
 */
public class ResultType<Result> {

    public ResultType(Result result, boolean ok) {
        this.result = result;
        this.ok = ok;
    }

    public ResultType(Result result) {
        this.result = result;
        this.ok = true;
    }

    public boolean isOk() {
        return ok;
    }

    public Result getResult() {
        return result;
    }
    
    
    private Result result;
    private boolean ok;
}
