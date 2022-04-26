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
    private static class CacheNode {
        private boolean invalid;
        private final Operator[] operators;
        private final long result, previousResult, addResult, blockResult;
        private final boolean strokeBlock, negateStroke, wasNegative;

        public CacheNode(boolean invalid, Operator[] operators, long result, long previous, boolean wasNegative) {
            this(invalid, operators, result, previous, 0, 1, true, false, wasNegative);
        }
    }

    private Map<CacheNode, CacheNode> result = new HashMap<>();
    private int index;

    private void generateA() {
        index = 0;
        push(new CacheNode(false, new Operator[numbers.length - 1], numbers[0], numbers[0], 0, numbers[0], false, false, false));
        do {
            Map<CacheNode, CacheNode> last = result;
            result = new HashMap<>();
            System.out.println("free " + last.size() + " cache nodes for gc");
            if (numbers.length == index + 1) {
                break;
            }
            //System.out.println();
            //System.out.println();
            //System.out.println("number: " + numbers[index + 1]);
            for (CacheNode cacheNode : last.values()) {
                //System.out.println("with: " + data.result);
                generateOperators(cacheNode, numbers[index + 1]);
                //System.out.println();
            }
            //System.out.println(result);
            index++;
        } while (!result.isEmpty());
    }

    private void push(CacheNode cacheNode) {
        //System.out.println(data.result + " " + data);
        if (index == numbers.length - 2) {
            if (cacheNode.invalid) {
                results.remove(cacheNode.result);
                duplicatedResults.add(cacheNode.result);
            } else {
                pushResult(cacheNode.result, cacheNode.operators);
            }
            return;
        }
        if (result.put(cacheNode, cacheNode) != null) {
            cacheNode.invalid = true;
        }
    }

    private void generateOperators(CacheNode cacheNode, long right) {
        Operator[] operators = cacheNode.operators.clone();
        //System.out.println(right);
        operators[index] = ADD;
        push(new CacheNode(
            cacheNode.invalid, operators.clone(), cacheNode.result + right, cacheNode.result, false));
        operators[index] = SUBTRACT;
        push(new CacheNode(
            cacheNode.invalid, operators.clone(), cacheNode.result - right, cacheNode.result, true));
        operators[index] = MULTIPLY;
        multiplication(cacheNode.invalid, operators.clone(), cacheNode.result, right, cacheNode.previousResult, cacheNode.addResult, cacheNode.blockResult,
            cacheNode.strokeBlock, cacheNode.negateStroke, cacheNode.wasNegative);
        operators[index] = DIVIDE;
        divide(cacheNode.invalid, operators, cacheNode.result, right, cacheNode.previousResult, cacheNode.addResult, cacheNode.blockResult,
            cacheNode.strokeBlock, cacheNode.negateStroke, cacheNode.wasNegative);
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
        push(new CacheNode(wasInvalid, operators, res, result, addResult, blockResult, false, negateStroke, false));
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
        push(new CacheNode(wasInvalid, operators, res, result, addResult, blockResult, false, negateStroke, false));
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
