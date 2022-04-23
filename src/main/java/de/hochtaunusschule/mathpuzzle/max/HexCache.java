package de.hochtaunusschule.mathpuzzle.max;

import de.hochtaunusschule.mathpuzzle.max.HexMove.Move;

/**
 * @author David (_Esel)
 */
public class HexCache {
    private static final int[] SIZES = new int[16];
    private static final Move[][] MOVES = new Move[16][];

    static {
        for (int i = 0; i < SIZES.length; i++) {
            SIZES[i] = HexBinary.letterSize(HexDisplay.DIGITS[i]);
        }
        for (int i = 0; i < MOVES.length; i++) {
            int source = HexDisplay.DIGITS[i];
            Move[] moves = new Move[MOVES.length];
            MOVES[i] = moves;
            for (int j = 0; j < MOVES.length; j++) {
                int destination = HexDisplay.DIGITS[j];
                moves[j] = HexMove.move(source, destination);
            }
        }
    }

    static Move move(int source, int destination) {
        return MOVES[source][destination];
    }

    static int letterSize(int number) {
        return SIZES[number];
    }

    static int totalLetterSize(int[] input) {
        int size = 0;
        for (int number : input) {
            size += SIZES[number];
        }
        return size;
    }
}
