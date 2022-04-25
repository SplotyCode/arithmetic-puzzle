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

/**
 * @author David (_Esel)
 */
public class ExpressionGenerator {
    private static final Random random = new Random();
    private final long[] numbers;
    private final Operator[] operators;
    private final TLongHashSet duplicatedResults = new TLongHashSet();
    private final TLongObjectHashMap<Operator[]> results = new TLongObjectHashMap<>();

    public ExpressionGenerator(int places) {
        numbers = new long[places];
        operators = new Operator[places - 1];
    }

    private void fillRandom() {
        numbers[0] = random.nextInt(9) + 1;
        if (numbers.length == 1) {
            return;
        }
        do {
            numbers[1] = random.nextInt(9) + 1;
        } while (numbers[0] % numbers[1] != 0);
        for (int i = 2; i < numbers.length; i++) {
            numbers[i] = random.nextInt(9) + 1;
        }
        //System.out.println("Using: " + Arrays.toString(numbers));
    }

    private ExpressionCandidates export() {
        Map<Long, Operator[]> export = new HashMap<>();
        results.forEachEntry((a, b) -> {
            export.put(a, b);
            return true;
        });
        return new ExpressionCandidates(numbers.clone(), export);
    }

    public ExpressionCandidates generate() {
        long[] strokeStore = new long[numbers.length];
        do {
            duplicatedResults.clear();
            results.clear();
            fillRandom();
            generateOperators(0, numbers[0], strokeStore, numbers[0], false, -1);
        } while (results.size() == 0);
        return export();
    }

    private void pushResult(long result) {
        if (result <= 0 || duplicatedResults.contains(result)) {
            return;
        }
        if (results.put(result, operators.clone()) != null) {
            results.remove(result);
            duplicatedResults.add(result);
        }
    }

    private void generateOperators(int index, long result,
                                   long[] results, long blockResult,
                                   boolean strokeBlock, int addStroke) {
        if (index == numbers.length - 1) {
            pushResult(result);
            return;
        }

        results[index] = result;
        int nextIndex = index + 1;
        long right = numbers[nextIndex];
        {
            operators[index] = ADD;
            generateOperators(nextIndex, result + right, results, blockResult, true, -1);
        }
        {
            operators[index] = SUBTRACT;
            generateOperators(nextIndex, result - right, results, blockResult, true, -1);
        }
        {
            operators[index] = MULTIPLY;
            if (strokeBlock) { /* vorher + - */
                blockResult = numbers[index];
                addStroke = index - 1;
            }
            blockResult *= right;
            long res = blockResult;
            if (addStroke != -1) {
                long add = operators[addStroke] == SUBTRACT ? -blockResult : blockResult;
                res = results[addStroke] + add;
            }
            generateOperators(nextIndex, res, results, blockResult, false, addStroke);
        }
        {
            operators[index] = DIVIDE;
            if (strokeBlock) { /* vorher + - */
                blockResult = numbers[index];
                addStroke = index - 1;
            }
            if (blockResult % right != 0) {
                return;
            }
            blockResult /= right;
            long res = blockResult;
            if (addStroke != -1) {
                long add = operators[addStroke] == SUBTRACT ? -blockResult : blockResult;
                res = results[addStroke] + add;
            }
            generateOperators(nextIndex, res, results, blockResult, false, addStroke);
        }
    }

    public static void main(String[] args) {
        int places = args.length == 0 ? 15 : Integer.parseInt(args[0]);
        ExpressionGenerator generator = new ExpressionGenerator(places);
        while (true) {
            long start = System.currentTimeMillis();
            generator.generate();
            System.out.println("Found in " + (System.currentTimeMillis() - start));
        }
    }
}
