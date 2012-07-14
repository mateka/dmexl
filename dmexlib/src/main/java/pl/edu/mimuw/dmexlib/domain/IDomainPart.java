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
public interface IDomainPart {
    public <T extends Collection> int getLeft(T a);
    public <T extends Collection> int getRight(T a);
}
