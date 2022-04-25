package de.hochtaunusschule.mathpuzzle.generate;

import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.ADD;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.DIVIDE;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.MULTIPLY;
import static de.hochtaunusschule.mathpuzzle.bruteforce.Operator.SUBTRACT;
import static org.junit.jupiter.api.Assertions.*;

import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author David (_Esel)
 */
class ExpressionGeneratorTest {
    private Operator[] operators;
    private long[] numbers;
    private long result;
    private boolean completed;

    @Test
    void oneUnitBasic() {
        runTest(new long[] {3}, new Operator[0], 3);
        runTest(new long[] {3, 1}, new Operator[] {ADD}, 4);
        runTest(new long[] {3, 1, 8, 4}, new Operator[] {ADD, ADD, ADD}, 16);
        runTest(new long[] {3, 1}, new Operator[] {SUBTRACT}, 2);
        runTest(new long[] {1, 3}, new Operator[] {SUBTRACT}, -2);
        runTest(new long[] {3, 1, 8, 4}, new Operator[] {ADD, SUBTRACT, ADD}, 0);
        runTest(new long[] {3, 4}, new Operator[] {MULTIPLY}, 12);
        runTest(new long[] {3, 4, 2, 1}, new Operator[] {MULTIPLY, MULTIPLY, MULTIPLY}, 24);
    }

    @Test
    void special() {
        runTest(
            new long[] {4 , 3 , 2 , 6 , 3 , 9 , 7 , 8 , 2 , 9 , 4 , 4 , 6 , 4 , 4 , 5},
            new Operator[] {MULTIPLY, MULTIPLY, MULTIPLY, MULTIPLY, MULTIPLY, ADD, MULTIPLY, DIVIDE, MULTIPLY, MULTIPLY, SUBTRACT, MULTIPLY, SUBTRACT, MULTIPLY, MULTIPLY},
            4792
        );
    }

    @Test
    void mix() {
        runTest(new long[] {3, 4, 2, 4}, new Operator[] {MULTIPLY, ADD, ADD}, 18);
        runTest(new long[] {3, 4, 2}, new Operator[] {ADD, MULTIPLY}, 11);
        runTest(new long[] {3, 3, 4, 2}, new Operator[] {ADD, ADD, MULTIPLY}, 14);
        runTest(new long[] {3, 3, 4, 2, 2}, new Operator[] {ADD, ADD, MULTIPLY, MULTIPLY}, 22);
        runTest(new long[] {3, 3, 4, 2}, new Operator[] {ADD, MULTIPLY, MULTIPLY}, 27);
        runTest(new long[] {3, 3, 4, 6}, new Operator[] {ADD, MULTIPLY, ADD}, 21);
        runTest(new long[] {3, 3, 4, 6, 2}, new Operator[] {ADD, MULTIPLY, ADD, MULTIPLY}, 27);
        runTest(new long[] {3, 4, 2, 2, 3}, new Operator[] {MULTIPLY, ADD, MULTIPLY, ADD}, 19);
        runTest(new long[] {3, 4, 2, 2, 3}, new Operator[] {ADD, MULTIPLY, MULTIPLY, ADD}, 22);

        runTest(new long[] {3, 4, 2, 4}, new Operator[] {MULTIPLY, SUBTRACT, SUBTRACT}, 6);
        runTest(new long[] {3, 4, 2}, new Operator[] {SUBTRACT, MULTIPLY}, -5);
        runTest(new long[] {3, 3, 4, 2}, new Operator[] {SUBTRACT, SUBTRACT, MULTIPLY}, -8);
        runTest(new long[] {3, 3, 4, 2, 2}, new Operator[] {SUBTRACT, SUBTRACT, MULTIPLY, MULTIPLY}, -16);
        runTest(new long[] {3, 3, 4, 2}, new Operator[] {SUBTRACT, MULTIPLY, MULTIPLY}, -21);
        runTest(new long[] {3, 3, 4, 6}, new Operator[] {SUBTRACT, MULTIPLY, SUBTRACT}, -15);
        runTest(new long[] {3, 3, 4, 6, 2}, new Operator[] {SUBTRACT, MULTIPLY, SUBTRACT, MULTIPLY}, -21);
        runTest(new long[] {3, 4, 2, 2, 3}, new Operator[] {MULTIPLY, SUBTRACT, MULTIPLY, SUBTRACT}, 5);
        runTest(new long[] {3, 4, 2, 2, 3}, new Operator[] {SUBTRACT, MULTIPLY, MULTIPLY, SUBTRACT}, -16);
    }

    private void runTest(long[] numbers, Operator[] operators, long result) {
        this.operators = operators;
        this.numbers = numbers;
        this.result = result;
        System.out.println("test start");
        completed = false;
        generateOperators(0, numbers[0], new long[numbers.length], numbers[0], false, -1);
        if (!completed) {
            fail("Not run");
        }
    }

    private void generateOperators(int index, long result,
                                   long[] results, long blockResult,
                                   boolean strokeBlock, int addStroke) {
        if (index == numbers.length - 1) {
            completed = true;
            Assertions.assertEquals(this.result, result);
            return;
        }

        results[index] = result;
        int nextIndex = index + 1;
        long right = numbers[nextIndex];
        if (operators[index] == ADD) {
            operators[index] = ADD;
            generateOperators(nextIndex, result + right, results, 1, true, -1);
        } else if (operators[index] == SUBTRACT) {
            operators[index] = SUBTRACT;
            generateOperators(nextIndex, result - right, results, 1, true, -1);
        } else if (operators[index] == MULTIPLY) {
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
        } else if (operators[index] == DIVIDE) {
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
}