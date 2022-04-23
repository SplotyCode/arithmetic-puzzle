package de.hochtaunusschule.mathpuzzle.generate;

import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.ADD;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.DIVIDE;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.MULTIPLY;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.SUBTRACT;

import de.hochtaunusschule.mathpuzzle.api.ExpressionCandidates;
import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.hash.TLongHashSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import org.checkerframework.checker.units.qual.C;

/**
 * @author David (_Esel)
 */
public class AsyncExpressionGenerator {
    private static final Random random = new Random();
    private final long[] numbers;
    private final int threadSplit = 1;
    private final TLongHashSet duplicatedResults = new TLongHashSet();
    private final TLongObjectHashMap<Operator[]> results = new TLongObjectHashMap<>();
    private final CountDownLatch countDownLatch = new CountDownLatch(1 * 4);

    public AsyncExpressionGenerator(int places) {
        numbers = new long[places];
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
            System.out.println("filll");
            blockStore[0] = numbers[0];
            generateOperators(0, numbers[0], new Operator[numbers.length - 1], strokeStore, blockStore, false, -1, false);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (results.size() == 0);
        return export();
    }

    private synchronized void pushResult(long result, Operator[] operators) {
        if (result <= 0 || duplicatedResults.contains(result)) {
            return;
        }
        if (results.put(result, operators.clone()) != null) {
            results.remove(result);
            duplicatedResults.add(result);
        }
    }

    private void generateOperators(int index, long result,
                                   Operator[] operators, long[] results, long[] blockStore,
                                   boolean strokeBlock, int addStroke, boolean split) {
        int stokeCopy = addStroke;
        if (index == numbers.length - 1) {
            pushResult(result, operators);
            return;
        } else if (!split && index == threadSplit) {
            Operator[] cloneOp = operators.clone();
            long[] cloneBlock = blockStore.clone();
            long[] cloneResults = results.clone();
            new Thread(() -> {
                generateOperators(index, result, cloneOp, cloneResults, cloneBlock, strokeBlock, stokeCopy, true);
                countDownLatch.countDown();
            }).start();
            return;
        }

        if (index == threadSplit) {
            System.out.println(Arrays.toString(operators));
        }

        results[index] = result;
        int nextIndex = index + 1;
        long right = numbers[nextIndex];
        {
            operators[index] = ADD;
            generateOperators(nextIndex, result + right, operators, results, blockStore, true, -1, split);
        }
        {
            operators[index] = SUBTRACT;
            generateOperators(nextIndex, result - right, operators, results, blockStore, true, -1, split);
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
                    throw new IllegalStateException("unexpected " + operators[index - 1]);
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
            generateOperators(nextIndex, res, operators, results, blockStore, false, addStroke, split);
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
                generateOperators(nextIndex, res, operators, results, blockStore, false, addStroke, split);
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
                generateOperators(nextIndex, res, operators, results, blockStore, false, addStroke, split);
            }
        }
    }

    public static void main(String[] args) {
        int places = args.length == 0 ? 15 : Integer.parseInt(args[0]);
        AsyncExpressionGenerator generator = new AsyncExpressionGenerator(places);
        while (true) {
            long start = System.currentTimeMillis();
            generator.generate();
            System.out.println("Found in " + (System.currentTimeMillis() - start));
        }
    }
}
