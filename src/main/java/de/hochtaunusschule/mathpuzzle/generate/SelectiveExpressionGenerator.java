package de.hochtaunusschule.mathpuzzle.generate;

import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.ADD;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.DIVIDE;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.MULTIPLY;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.SUBTRACT;

import de.hochtaunusschule.mathpuzzle.api.ExpressionCandidates;
import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.hash.TLongHashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author David (_Esel)
 */
public class SelectiveExpressionGenerator {
    private static final Random random = new Random();
    private final long[] numbers;
    private final TLongHashSet duplicatedResults = new TLongHashSet();
    private final TLongObjectHashMap<Operator[]> results = new TLongObjectHashMap<>();

    public SelectiveExpressionGenerator(int places) {
        numbers = new long[places];
    }

    private void fillRandom() {
        numbers[0] = random.nextInt(8) + 2;
        if (numbers.length == 1) {
            return;
        }
        do {
            numbers[1] = random.nextInt(8) + 2;
        } while (numbers[0] % numbers[1] != 0);
        for (int i = 2; i < numbers.length; i++) {
            numbers[i] = random.nextInt(8) + 2;
        }
    }

    public ExpressionCandidates generate() {
        do {
            duplicatedResults.clear();
            results.clear();
            fillRandom();
            generateA();
        } while (results.size() == 0);
        return export();
    }

    private ExpressionCandidates export() {
        Map<Long, Operator[]> export = new HashMap<>();
        results.forEachEntry((a, b) -> {
            export.put(a, b);
            return true;
        });
        return new ExpressionCandidates(numbers.clone(), export);
    }

    private void pushResult(long result, Operator[] operators) {
        if (result <= 0 || duplicatedResults.contains(result)) {
            return;
        }
        if (results.put(result, operators) != null) {
            results.remove(result);
            duplicatedResults.add(result);
        }
    }

    @ToString(exclude = "operators")
    @EqualsAndHashCode(exclude = "operators")
    @AllArgsConstructor
    private static class Data {
        private boolean invalid;
        private final Operator[] operators;
        private final long result, previousResult, addResult, blockResult;
        private final boolean strokeBlock, negateStroke, wasNegative;

        public Data(boolean invalid, Operator[] operators, long result, long previous, boolean wasNegative) {
            this(invalid, operators, result, previous, 0, 1, true, false, wasNegative);
        }
    }

    private Map<Data, Data> result = new HashMap<>();
    private int index;

    private void generateA() {
        index = 0;
        push(new Data(false, new Operator[numbers.length - 1], numbers[0], numbers[0], 0, numbers[0], false, false, false));
        do {
            Map<Data, Data> last = result;
            result = new HashMap<>();
            if (numbers.length == index + 1) {
                break;
            }
            for (Data data : last.values()) {
                generateOperators(data, numbers[index + 1]);
            }
            index++;
        } while (!result.isEmpty());
    }

    private void push(Data data) {
        if (index == numbers.length - 2) {
            if (data.invalid) {
                results.remove(data.result);
                duplicatedResults.add(data.result);
            } else {
                pushResult(data.result, data.operators);
            }
            return;
        }
        if (result.put(data, data) != null) {
            data.invalid = true;
        }
    }

    private void generateOperators(Data data, long right) {
        Operator[] operators = data.operators.clone();
        operators[index] = ADD;
        push(new Data(data.invalid, operators.clone(), data.result + right, data.result, false));
        operators[index] = SUBTRACT;
        push(new Data(data.invalid, operators.clone(), data.result - right, data.result, true));
        operators[index] = MULTIPLY;
        multiplication(data.invalid, operators.clone(), data.result, right, data.previousResult, data.addResult, data.blockResult,
            data.strokeBlock, data.negateStroke, data.wasNegative);
        operators[index] = DIVIDE;
        divide(data.invalid, operators, data.result, right, data.previousResult, data.addResult, data.blockResult,
            data.strokeBlock, data.negateStroke, data.wasNegative);
    }

    private void multiplication(boolean wasInvalid, Operator[] operators, long result, long right,
                                long previousResult, long addResult, long blockResult,
                                boolean strokeBlock, boolean negateStroke, boolean wasNegative) {
        long add = addResult;
        if (strokeBlock) { /* vorher + - */
            blockResult = numbers[index];
            add = previousResult;
            addResult = previousResult;
            negateStroke = wasNegative;
        }
        blockResult *= right;
        long res = negateStroke ? add - blockResult : add + blockResult;
        //System.out.println("* " + res + " " + right + " " + blockResult + " " + add);
        push(new Data(wasInvalid, operators, res, result, addResult, blockResult, false, negateStroke, false));
    }

    private void divide(boolean wasInvalid, Operator[] operators, long result, long right,
                        long previousResult, long addResult, long blockResult,
                        boolean strokeBlock, boolean negateStroke, boolean wasNegative) {
        long add = addResult;
        if (strokeBlock) { /* vorher + - */
            blockResult = numbers[index];
            add = previousResult;
            negateStroke = wasNegative;
        }
        if (blockResult % right != 0) {
            return;
        }
        blockResult /= right;
        long res = negateStroke ? add - blockResult : add + blockResult;
        push(new Data(wasInvalid, operators, res, result, addResult, blockResult, false, negateStroke, false));
    }

    public static void main(String[] args) {
        int places = args.length == 0 ? 15 : Integer.parseInt(args[0]);
        SelectiveExpressionGenerator generator = new SelectiveExpressionGenerator(places);
        while (true) {
            long start = System.currentTimeMillis();
            generator.generate();
            System.out.println("Found in " + (System.currentTimeMillis() - start));
        }
    }
}
