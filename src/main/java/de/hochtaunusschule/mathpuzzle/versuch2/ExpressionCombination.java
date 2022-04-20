package de.hochtaunusschule.mathpuzzle.versuch2;

import de.hochtaunusschule.mathpuzzle.math.Calculation;
import de.hochtaunusschule.mathpuzzle.math.Operator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author David (_Esel)
 */
public class ExpressionCombination {
    @RequiredArgsConstructor
    private static class Term {
        private final long left;
        private final long right;
        private final Operator[] leftOperators;
        private final Operator[] rightOperators;
        private final Operator operator;

        float interest() {
            return rateInterest(leftOperators) * rateInterest(rightOperators);
        }

        public int operators() {
            return leftOperators.length + rightOperators.length + 1;
        }
    }

    private static Set<Long> leftSideCandidates(long[] numbers) {
        Set<Long> candidates = new HashSet<>();
        for (int i = 1; i <= numbers.length; i++) {
            int start = numbers.length - i;
            leftSideCandidates(candidates, numbers, numbers[start], start + 1);
        }
        return candidates;
    }

    private static void leftSideCandidates(Set<Long> candidates, long[] numbers, long number, int index) {
        if (index == numbers.length) {
            candidates.add(number);
            return;
        }
        long right = numbers[index];
        index++;
        leftSideCandidates(candidates, numbers, number * right, index);
        if (!Calculation.validateDivide(number, right)) {
            leftSideCandidates(candidates, numbers, number / right, index);
        }
    }

    public static Expression combine(ExpressionGenerator left, ExpressionGenerator right) {
        while (true) {
            ExpressionCandidates candidates = left.generate();
            Set<Long> leftSideCandidates = leftSideCandidates(candidates.numbers());
            for (int i = 0; i < 100; i++) {
                Expression result = combine(leftSideCandidates, candidates, right.generate());
                if (result != null) {
                    return result;
                }
            }
        }
    }

    public static Expression combine(Set<Long> leftSideCandidates, ExpressionCandidates left, ExpressionCandidates right) {
        DuplicateTracker<Term> duplicateTracker = new DuplicateTracker<>();
        long[] numbers = ArrayUtils.addAll(left.numbers(), right.numbers());
        for (Map.Entry<Long, Operator[]> a : left.uniqueResults().entrySet()) {
            for (Map.Entry<Long, Operator[]> b : right.uniqueResults().entrySet()) {
                long sum = a.getKey() + b.getKey();
                long negate = a.getKey() - b.getKey();
                if (sum > 0) {
                    duplicateTracker.track(sum, new Term(a.getKey(), b.getKey(), a.getValue(), b.getValue(), Operator.ADD));
                }
                if (negate > 0) {
                    duplicateTracker.track(negate, new Term(a.getKey(), b.getKey(), a.getValue(), b.getValue(), Operator.SUBTRACT));
                }
            }
        }
        if (duplicateTracker.results().isEmpty()) {
            return null;
        }
        ExpressionGenerator generator = new ExpressionGenerator(right.numbers().length + 1);
        long[] number = generator.numbers();
        System.arraycopy(right.numbers(), 0, number, 1, right.numbers().length);
        for (long leftSide : leftSideCandidates) {
            number[0] = leftSide;
            ExpressionCandidates candidates = generator.generateSet();
            if (candidates == null) {
                continue;
            }
            for (long possibleResult : candidates.uniqueResults().keySet()) {
                duplicateTracker.invalidate(possibleResult);
            }
        }
        if (duplicateTracker.results().isEmpty()) {
            return null;
        }
        Map.Entry<Long, Term> result = duplicateTracker.results().entrySet().stream().max(
            Comparator.comparingDouble(value -> value.getValue().interest())).get();
        Term term = result.getValue();
        Operator[] operators = Arrays.copyOf(term.leftOperators, term.operators());
        operators[term.leftOperators.length] = term.operator;
        System.arraycopy(term.rightOperators, 0, operators, term.leftOperators.length + 1, term.rightOperators.length);
        return new Expression(result.getKey(), numbers, operators);
    }

    private static float rateInterest(Operator[] operators) {
        float goal = operators.length / 4F;
        float error = 0;
        for (Operator operator : Operator.values()) {
            int count = 0;
            for (Operator op : operators) {
                if (op == operator) {
                    count++;
                }
            }
            error += Math.abs(count - goal);
        }
        return 1 / error;
    }
}
