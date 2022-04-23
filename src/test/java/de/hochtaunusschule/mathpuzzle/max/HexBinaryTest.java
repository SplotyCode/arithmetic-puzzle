package de.hochtaunusschule.mathpuzzle.max;

import com.google.common.primitives.ImmutableIntArray;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * @author David (_Esel)
 */
public class HexBinaryTest {
    @Test
    void test() {
        System.out.println(HexBinary.letterSize(HexDisplay.DIGITS[0]));
        System.out.println(HexCache.totalLetterSize(HexBinary.extractHex("D24")));
        System.out.println(HexMove.change(HexBinary.DIGITS[13], HexBinary.DIGITS[14]));
        System.out.println(HexMove.move(HexBinary.DIGITS[13], HexBinary.DIGITS[14]));
        System.out.println(HexCache.move(13, 14));
    }

    @Test
    void test2() {
        //0xE4 0xE 1 0 0x4 2 9 5 1 0
        //[4]  14  1 0 [4] 2 9 5 1 0
        System.out.println(Arrays.toString(MadMax.go(
            new int[] {4},
            2 - 1,
            9 - 5,
            1 - 1,
            0 + 0
        )));
    }

    @Test
    void add() {
        System.out.println(Arrays.toString(
            ImmutableIntArray.builder().add(1).addAll(new int[] {2, 3}).build().toArray()));
    }
}
