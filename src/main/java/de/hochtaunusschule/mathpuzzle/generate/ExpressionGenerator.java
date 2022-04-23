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
        long[] blockStore = new long[numbers.length];
        do {
            duplicatedResults.clear();
            results.clear();
            fillRandom();
            blockStore[0] = numbers[0];
            generateOperators(0, numbers[0], strokeStore, blockStore, false, -1);
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
                                   long[] results, long[] blockStore,
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
            generateOperators(nextIndex, result + right, results, blockStore, true, -1);
        }
        {
            operators[index] = SUBTRACT;
            generateOperators(nextIndex, result - right, results, blockStore, true, -1);
        }
        {
            operators[index] = MULTIPLY;
            long res;
            if (strokeBlock) { /* vorher + - */
                long block = numbers[index] * right;
                blockStore[index + 1] = block;
                if (operators[index - 1] == SUBTRACT) {
                    res = results[index - 1] - block;
                } else if (operators[index - 1] == ADD) {
                    res = results[index - 1] + block;
                } else {
                    throw new IllegalStateException();
                }
                addStroke = index - 1;
            } else {
                res = blockStore[index] * right;
                blockStore[index + 1] = res;
                if (addStroke != -1) {
                    if (operators[addStroke] == SUBTRACT) {
                        res = results[addStroke] - res;
                    } else if (operators[addStroke] == ADD) {
                        res = results[addStroke] + res;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            generateOperators(nextIndex, res, results, blockStore, false, addStroke);
        }
        {
            operators[index] = DIVIDE;
            long res;
            if (strokeBlock) { /* vorher + - */
                if (numbers[index] % right != 0) {
                    return;
                }
                long block = numbers[index] / right;
                blockStore[index + 1] = block;
                if (operators[index - 1] == SUBTRACT) {
                    res = results[index - 1] - block;
                } else if (operators[index - 1] == ADD) {
                    res = results[index - 1] + block;
                } else {
                    throw new IllegalStateException();
                }
                addStroke = index - 1;
                generateOperators(nextIndex, res, results, blockStore, false, addStroke);
            } else {
                if (blockStore[index] % right != 0) {
                    return;
                }
                res = blockStore[index] / right;
                blockStore[index + 1] = res;
                if (addStroke != -1) {
                    if (operators[addStroke] == SUBTRACT) {
                        res = results[addStroke] - res;
                    } else if (operators[addStroke] == ADD) {
                        res = results[addStroke] + res;
                    } else {
                        throw new IllegalStateException();
                    }
                }
                generateOperators(nextIndex, res, results, blockStore, false, addStroke);
            }
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
