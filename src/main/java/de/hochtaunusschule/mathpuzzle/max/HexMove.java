package de.hochtaunusschule.mathpuzzle.max;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author David (_Esel)
 */
public interface HexMove extends HexBinary {
    @RequiredArgsConstructor
    class Change {
        /* set -> not set */
        public final int remove;
        /* not set -> set */
        private final int add;

        @Override
        public String toString() {
            return "Change{" +
                "remove=" + remove +
                ", add=" + add +
                '}';
        }
    }

    public static void main(String[] args) {
        System.out.println(move(HexDisplay.DIGITS[2], HexDisplay.DIGITS[0]));
    }

    @ToString
    @RequiredArgsConstructor
    class Move {
        public final int moves;
        /* consumed from outside */
        public final int fromOutside;
    }

    static Move move(int current, int desired) {
        Change change = change(current, desired);
        int remove = change.remove;
        int add = change.add;
        return new Move(add, add - remove);
    }

    static Change change(int current, int desired) {
        int remove = 0;
        int add = 0;
        for (int bit : HexBinary.BITS) {
            boolean hasLeft = HexBinary.state(bit, current);
            boolean hasRight = HexBinary.state(bit, desired);
            if (hasLeft && !hasRight) {
                remove++;
            } else if (!hasLeft && hasRight) {
                add++;
            }
        }
        return new Change(remove, add);
    }
}
