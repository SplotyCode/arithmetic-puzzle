package de.hochtaunusschule.mathpuzzle.max;

import org.junit.jupiter.api.Assertions;
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
    void edgeCases() {
        Assertions.assertEquals(MadMax.solve("11111111", 300), "11111111");
        Assertions.assertEquals(MadMax.solve("EEEE11111111", 300), "ffe811111111");
        Assertions.assertEquals(MadMax.solve("EEEE11111111", 0), "eeee11111111");
    }

    @Test
    void testProphet() {
        Assertions.assertEquals(
            HexBinary.toHexString(HexBinary.extractHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF88EFA9EBE89EFA99FBDAA8E8EAD88AB899F8E8F9AA9E9AD88988EDA9A99888EDAD989A8BAFD8A888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888")),
            "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff88efa9ebe89efa99fbdaa8e8ead88ab899f8e8f9aa9e9ad88988eda9a99888edad989a8bafd8a888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888"
        );
    }

    @Test
    void bwInf() {
        Assertions.assertEquals(
            MadMax.solve("1A02B6B50D7489D7708A678593036FA265F2925B21C28B4724DD822038E3B4804192322F230AB7AF7BDA0A61BA7D4AD8F888", 87),
            "ffffffffffffffffffffffffffffffffffffffeb8de88baa8add888898e9ba88ad98988f898ab7af7bda8a61ba7d4ad8f888"
        );
    }
}
