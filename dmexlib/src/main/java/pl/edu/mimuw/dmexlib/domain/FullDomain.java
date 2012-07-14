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
public class FullDomain implements IDomainPart {

    @Override
    public <T extends Collection> int getLeft(T a) {
        return 0;
    }

    @Override
    public <T extends Collection> int getRight(T a) {
        return a.size();
    }
    
}
