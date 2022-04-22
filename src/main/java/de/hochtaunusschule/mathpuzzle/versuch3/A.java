package de.hochtaunusschule.mathpuzzle.versuch3;

import de.hochtaunusschule.mathpuzzle.math.Calculation;
import de.hochtaunusschule.mathpuzzle.math.Operator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.units.qual.C;

/**
 * @author David (_Esel)
 */
public class A {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        aa();
        System.out.println((System.currentTimeMillis() - start));
    }

    private static void c(Set<Long> input) {
        Set<Long> duplicates = new HashSet<>();
        Map<Long, String> results = new HashMap<>();
        for (Operator operator : Operator.values()) {
            for (long i : input) {
                for (long j : input) {
                    if (operator == Operator.DIVIDE && Calculation.validateDivide(i, j)) {
                        continue;
                    }
                    long result = operator.applyAsLong(i, j);
                    if (duplicates.contains(result)) {
                        continue;
                    }
                    if (results.put(result, i + operator.symbolAsString() + j) != null) {
                        results.remove(result);
                        duplicates.add(result);
                    }
                }
            }
        }
        List<Long> keySet = new ArrayList<>(results.keySet());
        Collections.sort(keySet);
        System.out.println("--");
        System.out.println(keySet);
    }

    private static void b(Set<Long> input) {
        Set<Long> duplicates = new HashSet<>();
        Map<Long, String> results = new HashMap<>();
        for (Operator operator : Operator.values()) {
            for (long i : input) {
                for (long j : input) {
                    if (operator == Operator.DIVIDE && Calculation.validateDivide(i, j)) {
                        continue;
                    }
                    long result = operator.applyAsLong(i, j);
                    if (duplicates.contains(result)) {
                        continue;
                    }
                    if (results.put(result, i + operator.symbolAsString() + j) != null) {
                        results.remove(result);
                        duplicates.add(result);
                    }
                }
            }
        }
        List<Long> keySet = new ArrayList<>(results.keySet());
        Collections.sort(keySet);
        System.out.println(keySet);
        c(results.keySet());
    }

    private static void aa() {
        Set<Long> duplicates = new HashSet<>();
        Map<Long, String> results = new HashMap<>();
        for (Operator operator1 : Operator.values()) {
            for (Operator operator2 : Operator.values()) {
                for (Operator operator3 : Operator.values()) {
                    for (Operator operator4 : Operator.values()) {
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                for (int k = 0; k < 10; k++) {
                                    for (int l = 0; l < 10; l++) {
                                        for (int m = 0; m < 10; m++) {
                                            Calculation calculation =
                                                new Calculation(new long[] {i, j, k, l, m},
                                                    new Operator[] {operator1, operator2, operator3, operator4});
                                            if (calculation.calculate()) {
                                                continue;
                                            }
                                            long result = calculation.result();
                                            if (duplicates.contains(result)) {
                                                continue;
                                            }
                                            if (results.put(result, i + operator1.symbolAsString() + j +
                                                operator2.symbolAsString() + k +
                                                operator3.symbolAsString() + l + operator4.symbolAsString() + m) != null) {
                                                results.remove(result);
                                                duplicates.add(result);
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        List<Long> keySet = new ArrayList<>(results.keySet());
        Collections.sort(keySet);
        System.out.println(keySet.size());
    }

    private static void a() {
        Set<Long> duplicates = new HashSet<>();
        Map<Long, String> results = new HashMap<>();
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
                    if (results.put(result, i + operator.symbolAsString() + j) != null) {
                        results.remove(result);
                        duplicates.add(result);
                    }
                }
            }
        }
        b(results.keySet());
    }
}
