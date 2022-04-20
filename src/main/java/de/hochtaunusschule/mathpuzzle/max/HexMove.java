package de.hochtaunusschule.mathpuzzle.max;

import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
public interface HexMove extends HexBinary {
    @RequiredArgsConstructor
    class Change {
        public final int remove, add;

        @Override
        public String toString() {
            return "Change{" +
                "remove=" + remove +
                ", add=" + add +
                '}';
        }
    }

    static Change change(int left, int right) {
        int remove = 0;
        int add = 0;
        for (int bit : HexBinary.BITS) {
            boolean hasLeft = HexBinary.state(bit, left);
            boolean hasRight = HexBinary.state(bit, right);
            if (hasLeft && !hasRight) {
                remove++;
            } else if (!hasLeft && hasRight) {
                add++;
            }
        }
        return new Change(remove, add);
    }
}
