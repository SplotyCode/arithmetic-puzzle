package de.hochtaunusschule.mathpuzzle.attempt.cache;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import de.hochtaunusschule.mathpuzzle.api.PuzzleConstrains;
import de.hochtaunusschule.mathpuzzle.bruteforce.Calculation;
import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author David (_Esel)
 */
public class ExpressionCache {
    private static final Operator[] OPERATORS = Operator.values();
    private static final int MIN_OPERATOR_ID = 0;
    private static final int MAX_OPERATOR_ID = Operator.DIVIDE.ordinal();
    private final List<Expression> expressions = new ArrayList<>();
    private final Map<Integer, TreeMultimap<Long, Expression>> expression = new HashMap<>();
    private final HashMultimap<Long, Expression> a = HashMultimap.create();
    private TreeMultimap<Long, Expression> stage;
    private HashMultiset<Long> results;
    private long[] tempNumbers;
    private Operator[] tempOperators;
    private long counter;

    private void generateNumbers() {
        Random random = new Random();
        long start = System.currentTimeMillis();
        for (int i = 8; i <= 8;) {
            stage = TreeMultimap.create(Ordering.natural(), Ordering.allEqual());
            expression.put(i, stage);
            tempNumbers = new long[i];
            tempOperators = new Operator[i - 1];
            long start2 = System.currentTimeMillis();
            System.out.println("Starting operators: ");
            for (int j = 0; j < i; j++) {
                tempNumbers[j] = random.nextInt(10);
            }
            generateOperators();
            System.out.println("Finished " + (System.currentTimeMillis() - start2));
            System.out.println("results in " + i + " " + stage.keySet().size());
        }
        System.out.println("results  ");
        long number = expressions.stream()
            .filter(expression1 -> expression1.duplicates() == 1)
            .filter(expression1 -> expression1.result() >= 0)
            .count();
        System.out.println(number + " " + expressions.size());
        long delay = System.currentTimeMillis() - start;
        System.out.println("Took " + delay + " to build ExpressionCache");
    }

    private void generateNumbers(int index) {
        if (index == tempNumbers.length) {
            long start = System.currentTimeMillis();
            System.out.println("Starting operators: ");
            generateOperators();
            System.out.println("Finished " + (System.currentTimeMillis() - start));
            return;
        }
        for (int i = 0; i <= PuzzleConstrains.MAX_DIGIT; i++) {
            tempNumbers[index] = i;
            generateNumbers(index + 1);
        }
    }

    private void generateOperators() {
        results = HashMultiset.create();
        generateOperators(0);
    }

    private void generateOperators(int index) {
        if (index == tempNumbers.length - 1) {
            long[] numbers = tempNumbers.clone();
            Operator[] operators = tempOperators.clone();
            Calculation calculation = new Calculation(numbers.clone(), operators);
            if (calculation.calculate()) {
                return;
            }
            long result = calculation.result();

            //a.put(result, new Expression(numbers, operators, result, results));
            results.add(result);
            expressions.add(new Expression(numbers, operators, result, results));
            //stage.put(result, new Expression(numbers, operators, result, results));
            counter++;
            return;
        }
        for (int i = 0; i <= MAX_OPERATOR_ID; i++) {
            Operator operator = OPERATORS[i];
            tempOperators[index] = operator;
            generateOperators(index + 1);
        }
    }

    public static void main(String[] args) {
        ExpressionCache cache = new ExpressionCache();
        cache.generateNumbers();
        System.out.println(cache.counter);
    }
}
