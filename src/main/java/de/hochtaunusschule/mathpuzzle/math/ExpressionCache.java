package de.hochtaunusschule.mathpuzzle.math;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author David (_Esel)
 */
public class ExpressionCache {
    private static final Operator[] OPERATORS = Operator.values();
    private static final int MIN_OPERATOR_ID = 0;
    private static final int MAX_OPERATOR_ID = Operator.DIVIDE.ordinal();
    private final Map<Integer, TreeMultimap<Long, Expression>> expression = new HashMap<>();
    private TreeMultimap<Long, Expression> stage;
    private long[] tempNumbers;
    private Operator[] tempOperators;
    private long counter;

    private void generateNumbers() {
        for (int i = 2; i <= 4; i++) {
            stage = TreeMultimap.create(Ordering.natural(), Ordering.allEqual());
            tempNumbers = new long[i];
            tempOperators = new Operator[i - 1];
            generateNumbers(0, PuzzleConstrains.MIN_DIGIT);
            System.out.println("results in " + i + " " + stage.keySet().size());
        }
    }

    private void generateNumbers(int index, int start) {
        if (index == tempNumbers.length) {
            generateOperators();
            return;
        }
        for (int i = start; i <= PuzzleConstrains.MAX_DIGIT; i++) {
            tempNumbers[index] = i;
            generateNumbers(index + 1, i);
        }
    }

    private void generateOperators() {
        generateOperators(0, MIN_OPERATOR_ID);
    }

    private void generateOperators(int index, int start) {
        if (index == tempNumbers.length - 1) {
            long[] numbers = tempNumbers.clone();
            Operator[] operators = tempOperators.clone();
            Calculation calculation = new Calculation(numbers, operators);
            if (calculation.calculate()) {
                return;
            }
            long result = calculation.result();
            stage.put(result, new Expression(numbers, operators, stage.keySet()));
            counter++;
            return;
        }
        for (int i = start; i <= MAX_OPERATOR_ID; i++) {
            Operator operator = OPERATORS[i];
            tempOperators[index] = operator;
            generateOperators(index + 1, i);
        }
    }

    public static void main(String[] args) {
        ExpressionCache cache = new ExpressionCache();
        cache.generateNumbers();
        System.out.println(cache.counter);
    }
}
