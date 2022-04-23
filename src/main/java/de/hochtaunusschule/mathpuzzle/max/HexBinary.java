package de.hochtaunusschule.mathpuzzle.max;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author David (_Esel)
 */
public interface HexBinary extends HexDisplay{
    int[] BITS = new int[] {UP_LEFT, UP_MID, UP_RIGHT, CENTER, DOWN_LEFT, DOWN_MID, DOWN_RIGHT};

    static boolean state(int bit, int number) {
        return (number & bit) != 0;
    }

    static char stateChar(int bit, char ch, int number) {
        return state(bit, number) ? ch : ' ';
    }

    static int difference(int left, int right) {
        int difference = 0;
        for (int bit : BITS) {
            if (state(bit, left) == state(bit, right)) {
                difference++;
            }
        }
        return difference;
    }

    static int letterSize(int number) {
        int size = 0;
        for (int bit : BITS) {
            if (state(bit, number)) {
                size++;
            }
        }
        return size;
    }

    static int[] extractHex(String hex) {
        return hex
            .chars()
            .map(operand -> Integer.parseInt(String.valueOf((char) operand), 16))
            .toArray();
    }

    static String toHexString(int[] array) {
        return Arrays.stream(array)
            .mapToObj(Integer::toHexString)
            .collect(Collectors.joining());
    }

    static void print(int number, PrintStream stream) {
        stream.println(" " + stateChar(UP_MID, '-', number));
        stream.println(stateChar(UP_LEFT, '|', number) + " " + stateChar(UP_RIGHT, '|', number));
        stream.println(" " + stateChar(CENTER, '-', number));
        stream.println(stateChar(DOWN_LEFT, '|', number) + " " + stateChar(DOWN_RIGHT, '|', number));
        stream.println(" " + stateChar(DOWN_MID, '-', number));
    }
}
