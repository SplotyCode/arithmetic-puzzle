package de.hochtaunusschule.mathpuzzle.generate;

import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.*;

import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import de.hochtaunusschule.mathpuzzle.bruteforce.ExpressionCandidates;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author David (_Esel)
 */
public class ExpressionGenerator {
    private static final Random random = new Random();
    private final long[] numbers;
    private final Operator[] operators;
    private final Set<Long> duplicatedResults = new HashSet<>();
    private final Map<Long, Operator[]> results = new HashMap<>();

    public ExpressionGenerator(int places) {
        numbers = new long[places];
        operators = new Operator[places - 1];
    }

    private void fillRandom() {
        numbers[0] = random.nextInt(9) + 1;
        do {
            numbers[1] = random.nextInt(9) + 1;
        } while (numbers[0] % numbers[1] != 0);
        for (int i = 2; i < numbers.length; i++) {
            numbers[i] = random.nextInt(9) + 1;
        }
        //System.out.println("Using: " + Arrays.toString(numbers));
    }

    private ExpressionCandidates export() {
        return new ExpressionCandidates(numbers.clone(), results);
    }

    long[] numbers() {
        return numbers;
    }

    public ExpressionCandidates generateSet() {
        //generateOperators(0);
        if (results.size() == 0) {
            return null;
        }
        return export();
    }

    private static long tryied;
    private static long good;

    public ExpressionCandidates generate() {
        long[] strokeStore = new long[numbers.length];
        long[] blockStore = new long[numbers.length];
        do {
            duplicatedResults.clear();
            results.clear();
            fillRandom();
            System.out.println("try with numbers: " + Arrays.toString(numbers));
            generateOperators(0, numbers[0], strokeStore, blockStore, false, -1);
            tryied++;
        } while (results.size() == 0);
        /*results.forEach((aLong, operators) -> {
            System.out.println(aLong + " " + Arrays.toString(operators));
        });
        System.out.println(results);
        System.out.println("^^ was good");
        good++;
        System.out.println(good + " / " + tryied + " " + (good / (float) tryied));*/
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

    //1+2+3*4*5*6*7+8+9+10*11*12+13
    private static final Operator[] TEST_OP = new Operator[] {
        MULTIPLY, MULTIPLY, MULTIPLY, MULTIPLY, MULTIPLY, ADD, SUBTRACT, MULTIPLY, /*SUBTRACT, ADD, SUBTRACT, MULTIPLY, ADD, SUBTRACT*/
    };

    private void generateOperators(int index, long result,
                                   long[] strokeStore, long[] blockStore,
                                   boolean strokeBlock, int addStroke) {
        if (index == numbers.length - 1) {
            pushResult(result);
            return;
        }

        strokeStore[index] = result;
        if (index == 0) {
            blockStore[0] = numbers[0];
        }
        int nextIndex = index + 1;
        long right = numbers[nextIndex];
        {
            operators[index] = ADD;
            generateOperators(nextIndex, result + right, strokeStore, blockStore, true, -1);
        }
        {
            operators[index] = SUBTRACT;
            generateOperators(nextIndex, result - right, strokeStore, blockStore, true, -1);
        }
        {
            operators[index] = MULTIPLY;
            long res;
            if (strokeBlock) { /* vorher + - */
                long block = numbers[index] * right;
                blockStore[index + 1] = block;
                if (operators[index - 1] == SUBTRACT) {
                    res = strokeStore[index - 1] - block;
                } else if (operators[index - 1] == ADD) {
                    res = strokeStore[index - 1] + block;
                } else {
                    throw new IllegalStateException();
                }
                addStroke = index - 1;
            } else {
                res = blockStore[index] * right;
                blockStore[index + 1] = res;
                if (addStroke != -1) {
                    if (operators[addStroke] == SUBTRACT) {
                        res = strokeStore[addStroke] - res;
                    } else if (operators[addStroke] == ADD) {
                        res = strokeStore[addStroke] + res;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            generateOperators(nextIndex, res, strokeStore, blockStore, false, addStroke);
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
                    res = strokeStore[index - 1] - block;
                } else if (operators[index - 1] == ADD) {
                    res = strokeStore[index - 1] + block;
                } else {
                    throw new IllegalStateException();
                }
                addStroke = index - 1;
                generateOperators(nextIndex, res, strokeStore, blockStore, false, addStroke);
            } else {
                if (blockStore[index] % right != 0) {
                    return;
                }
                res = blockStore[index] / right;
                blockStore[index + 1] = res;
                if (addStroke != -1) {
                    if (operators[addStroke] == SUBTRACT) {
                        res = strokeStore[addStroke] - res;
                    } else if (operators[addStroke] == ADD) {
                        res = strokeStore[addStroke] + res;
                    } else {
                        throw new IllegalStateException();
                    }
                }
                generateOperators(nextIndex, res, strokeStore, blockStore, false, addStroke);
            }
        }
    }

    public static void main(String[] args) {
        ExpressionGenerator generator = new ExpressionGenerator(15);
        //generator.generate();
        //generator.generateOperators(0, 5, new long[9], false, -1);
        while (true) {
            long start = System.currentTimeMillis();
            generator.generate();
            System.out.println("Found in " + (System.currentTimeMillis() - start));
        }
    }
}
