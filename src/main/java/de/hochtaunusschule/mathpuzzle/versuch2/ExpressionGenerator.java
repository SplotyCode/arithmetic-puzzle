package de.hochtaunusschule.mathpuzzle.versuch2;

import de.hochtaunusschule.mathpuzzle.math.Calculation;
import de.hochtaunusschule.mathpuzzle.math.Operator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author David (_Esel)
 */
public class ExpressionGenerator {
    private static final Operator[] OPERATORS = Operator.values();
    private static final int MAX_OPERATOR_ID = Operator.DIVIDE.ordinal();
    private static final Random random = new Random();
    private final long[] numbers;
    private final Operator[] tempOperators;
    private final Set<Long> duplicatedResults = new HashSet<>();
    private final Map<Long, Operator[]> results = new HashMap<>();

    public ExpressionGenerator(int places) {
        numbers = new long[places];
        tempOperators = new Operator[places - 1];
    }

    private void fillRandom() {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(10);
        }
    }

    private ExpressionCandidates export() {
        return new ExpressionCandidates(numbers.clone(), results);
    }

    long[] numbers() {
        return numbers;
    }

    public ExpressionCandidates generateSet() {
        generateOperators(0);
        if (results.size() == 0) {
            return null;
        }
        return export();
    }

    public ExpressionCandidates generate() {
        duplicatedResults.clear();
        results.clear();
        while (results.size() == 0) {
            duplicatedResults.clear();
            results.clear();
            fillRandom();
            generateOperators(0);
        }
        return export();
    }

    private void generateOperators(int index) {
        if (index == numbers.length - 1) {
            Calculation calculation = new Calculation(numbers.clone(), tempOperators.clone());
            if (calculation.calculate()) {
                return;
            }
            long result = calculation.result();
            if (duplicatedResults.contains(result)) {
                return;
            }
            if (results.put(result, tempOperators.clone()) != null) {
                results.remove(result);
                duplicatedResults.add(result);
            }
            return;
        }
        for (int i = 0; i <= MAX_OPERATOR_ID; i++) {
            Operator operator = OPERATORS[i];
            tempOperators[index] = operator;
            generateOperators(index + 1);
        }
    }

    public static void main(String[] args) {
        ExpressionGenerator generator = new ExpressionGenerator(15);
        while (true) {
            long start = System.currentTimeMillis();
            generator.generate();
            System.out.println((System.currentTimeMillis() - start));
        }
    }
}
