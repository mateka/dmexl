/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static pl.edu.mimuw.dmexlib.utils.BSetOps.*;

/**
 *
 * @author Mateusz
 */
public class Position {
    
    public Position(int dimensions) {
        subset = new BitSet(dimensions);
        subset.clear();
    }
    
    public Position(int dimensions, Random rnd) {
        subset = new BitSet(dimensions);
        for (int i = 0; i < dimensions; ++i) {
            if (rnd.nextDouble() < 0.5) {
                subset.set(i);
            }
        }
    }
    
    public Position(Position other) {
        subset = (BitSet) other.subset.clone();
    }
    
    public int cardinality() {
        return subset.cardinality();
    }
    
    public int dimensions() {
        return subset.size();
    }
    
    public int distanceTo(Position other) {
        final Difference d = diff(other.subset, subset);
        return d.left.cardinality() - d.right.cardinality();
    }
    
    public void update(Position best, int v, Random rnd) {
        final int different = sym_minus(best.subset, subset).cardinality();

        // Randomly change min(v, different) bits in position,
        // which are different than in the best position
        flipInto(subset, randomSubset(xor(best.subset, subset), v, rnd));

        // If different < v, then change random bits outside best
        int n = v - different;
        if (0 < n) {
            flipInto(subset, randomSubset(minus(subset, best.subset), n, rnd));
        }
    }
    
    public Map<Integer, Integer> mapping() {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        
        for (int i = 0, last = 0; i < subset.cardinality(); ++i) {
            last = subset.nextSetBit(last);
            result.put(i, last);
        }
        
        return result;
    }

    @Override
    public String toString() {
        return subset.toString();
    }
    
    
    
    private final BitSet subset;
}
