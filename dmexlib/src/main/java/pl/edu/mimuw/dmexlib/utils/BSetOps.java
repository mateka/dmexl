/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib.utils;

import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author Mateusz
 */
public class BSetOps {

    static public BitSet and(final BitSet a, final BitSet b) {
        BitSet r = (BitSet) a.clone();
        r.and(b);
        return r;
    }

    static public Difference diff(final BitSet a, final BitSet b) {
        return new Difference(a, b);
    }

    static public BitSet minus(final BitSet a, final BitSet b) {
        Difference d = diff(a, b);
        return d.left;
    }

    static public BitSet sym_minus(final BitSet a, final BitSet b) {
        Difference d = diff(a, b);
        return or(d.left, d.right);
    }

    static public BitSet or(final BitSet a, final BitSet b) {
        BitSet r = (BitSet) a.clone();
        r.or(b);
        return r;
    }

    static public BitSet xor(final BitSet a, final BitSet b) {
        BitSet r = (BitSet) a.clone();
        r.xor(b);
        return r;
    }

    static public void flipInto(BitSet a, BitSet b) {
        for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i+1)) {
            a.flip(i);
        }
    }

    static public BitSet flip(BitSet a, BitSet b) {
        BitSet result = (BitSet) a.clone();
        flipInto(result, b);
        return result;
    }

    static public BitSet randomSubset(BitSet set, int m, Random rnd) {
        BitSet result = new BitSet(set.size());

        final int n = set.cardinality();

        for (int i = set.nextSetBit(0), v = 0; 0 < m && i >= 0; i = set.nextSetBit(i+1), ++v) {
            if (rnd.nextDouble() < ((double) m) / (n - v)) {
                result.set(i);
                m--;
            }
        }

        return result;
    }

    static public class Difference {

        public Difference(BitSet a, BitSet b) {
            final BitSet xor = xor(a, b);

            this.left = and(a, xor);
            this.right = and(b, xor);
        }

        public final BitSet left;
        public final BitSet right;
    }

}
