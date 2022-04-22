package de.hochtaunusschule.mathpuzzle.versuch3;

import de.hochtaunusschule.mathpuzzle.math.Calculation;
import de.hochtaunusschule.mathpuzzle.math.Operator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
public class TupleGen {
    private static final Random random = new Random();
    private static final List<Multiplication> tuples = new ArrayList<>(Arrays.asList(
        new Multiplication(8, 8, 64),
        new Multiplication(7, 7, 49),
        new Multiplication(9, 9, 81),
        new Multiplication(5, 5, 25)
    ));

    public static Multiplication random() {
        return tuples.get(random.nextInt(tuples.size()));
    }

    /*static {
        Map<Long, Multiplication> tuples = new HashMap<>();
        Set<Long> duplicates = new HashSet<>();
        for (Operator operator : Operator.values()) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (operator == Operator.DIVIDE && Calculation.validateDivide(i, j)) {
                        continue;
                    }
                    long result = operator.applyAsLong(i, j);
                    if (duplicates.contains(result)) {
                        continue;
                    }
                    if (tuples.put(result, new Multiplication(i, j, result)) != null) {
                        tuples.remove(result);
                        duplicates.add(result);
                    }
                }
            }
        }
        TupleGen.tuples.addAll(tuples.values());
    }*/

    @RequiredArgsConstructor
    public static class Multiplication {
        public final long left, right;
        public final long result;
    }
}
