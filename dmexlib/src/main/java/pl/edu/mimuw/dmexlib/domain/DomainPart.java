/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.domain;

import java.util.Collection;

/**
 *
 * @author matek
 */
public class DomainPart implements IDomainPart {

    public DomainPart(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public <T extends Collection> int getLeft(T a) {
        return left;
    }

    @Override
    public <T extends Collection> int getRight(T a) {
        return right;
    }
    private int left, right;
}
