package de.hochtaunusschule.mathpuzzle.max;

import java.math.BigInteger;
import java.util.Arrays;
import lombok.AllArgsConstructor;

/**
 * @author David (_Esel)
 */
@AllArgsConstructor
public class MadMax implements HexBinary {
    private static int[] start;

    private static boolean checkDowngrade(int[] input) {
        for (int i = 0; i < start.length; i++) {
            if (start[i] > input[i]) {
                return true;
            }
        }
        return false;
    }

    private static void solve(int[] input, int index, int bladesOver0, int bladesReserved0, int stepsLeft0) {
        if (index == input.length) {
            if (bladesOver0 == 0 && bladesReserved0 == 0) {
                System.out.println("Solution " + Arrays.toString(input) + " " + HexBinary.toHexString(input));
            } else {
                System.out.println("Ignore solution as blades are left");
            }
            return;
        }
        System.out.println("Before place " + index + " " + Arrays.toString(input) + " " + Integer.parseInt(HexBinary.toHexString(input), 16));
        int number = input[index];
        int segmend = DIGITS[number];
        for (int upgrade = DIGITS.length - 1; upgrade >= 0; upgrade--) {
            int bladesOver = bladesOver0;
            int bladesReserved = bladesReserved0;
            int stepsLeft = stepsLeft0;
            HexMove.Change change = HexMove.change(segmend, DIGITS[upgrade]);
            int needAdd = change.add;
            int needGive = change.remove;
            System.out.println("Try move " + number + " -> " + upgrade + " (" + change + ")");
            int useBlade = Math.min(bladesOver, needAdd);
            System.out.println("Using blades: " + useBlade);
            bladesOver -= useBlade;
            needAdd -= useBlade;
            int useSteps = Math.min(stepsLeft, Math.min(needAdd, needGive));
            System.out.println("using steps:  " + useSteps);
            stepsLeft -= useSteps;
            needAdd -= useSteps;
            needGive -= useSteps;
            int reserveBlade =
            input[index] = upgrade;
            System.out.println("blades: +" + needGive);
            bladesReserved += needGive;
            if (checkDowngrade(input)) {
                continue;
            }
            System.out.println(number + " to " + upgrade + " " + stepsLeft + " " + needAdd + " " + needGive);
            solve(input, index + 1, bladesOver, bladesReserved, stepsLeft);
        }
    }

    public static void main(String[] args) {
        System.out.println(new BigInteger("EF50AA77ECAD25F5E11A307B713EAAEC55215E7E640FD263FA529BBB48DC8FAFE14D5B02EBF792B5CCBBE9FA1330B867E330A6412870DD2BA6ED0DBCAE553115C9A31FF350C5DF993824886DB5111A83E773F23AD7FA81A845C11E22C4C45005D192ADE68AA9AA57406EB0E7C9CA13AD03888F6ABEDF1475FE9832C66BFDC28964B7022BDD969E5533EA4F2E4EABA75B5DC11972824896786BD1E4A7A7748FDF1452A5079E0F9E6005F040594185EA03B5A869B109A283797AB31394941BFE4D38392AD12186FF6D233585D8C820F197FBA9F6F063A0877A912CCBDCB14BEECBAEC0ED061CFF60BD517B6879B72B9EFE977A9D3259632C718FBF45156A16576AA7F9A4FAD40AD8BC87EC569F9C1364A63B1623A5AD559AAF6252052782BF9A46104E443A3932D25AAE8F8C59F10875FAD3CBD885CE68665F2C826B1E1735EE2FDF0A1965149DF353EE0BE81F3EC133922EF43EBC09EF755FBD740C8E4D024B033F0E8F3449C94102902E143433262CDA1925A2B7FD01BEF26CD51A1FC22EDD49623EE9DEB14C138A7A6C47B677F033BDEB849738C3AE5935A2F54B99237912F2958FDFB82217C175448AA8230FDCB3B3869824A826635B538D47D847D8479A88F350E24B31787DFD60DE5E260B265829E036BE340FFC0D8C05555E75092226E7D54DEB42E1BB2CA9661A882FB718E7AA53F1E606", 16));
        int[] input = HexBinary.extractHex("D24");
        start = input.clone();
        solve(input, 0, 0, 0, 3);
        System.out.println(Integer.parseInt("EE4", 16));
    }
}
