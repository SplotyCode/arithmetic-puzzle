package de.hochtaunusschule.mathpuzzle.investigate;

import java.util.Arrays;

/**
 * @author David (_Esel)
 */
public class Test {
    private static void permutation(int[] perm, int pos) {
        if (pos == perm.length) {
            System.out.println(Arrays.toString(perm));
        } else {
            for (int i = 0 ; i < 9 ; i++) {
                perm[pos] = i;
                permutation(perm, pos + 1);
            }
        }
    }

    public static void main(String[] args) {
        permutation(new int[5], 0);
    }

}
