package de.hochtaunusschule.mathpuzzle.versuch2;

import de.hochtaunusschule.mathpuzzle.math.Calculation;
import de.hochtaunusschule.mathpuzzle.math.Operator;
import de.hochtaunusschule.mathpuzzle.view.WebController;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @author David (_Esel)
 */
public class BruteforceSolver {
    private static final Operator[] OPERATORS = Operator.values();
    private static final int MIN_OPERATOR_ID = 0;
    private static final int MAX_OPERATOR_ID = Operator.DIVIDE.ordinal();
    private final long[] numbers;
    private final long result;
    private final Operator[] operators;

    public BruteforceSolver(long[] numbers, long result) {
        this.numbers = numbers;
        this.result = result;
        operators = new Operator[numbers.length - 1];
    }

    private void generateOperators(int index) {
        if (index == operators.length) {
            Calculation calculation = new Calculation(numbers.clone(), operators.clone());
            if (calculation.calculate()) {
                return;
            }
            if (result == calculation.result()) {
                System.out.println("Found combination " + Arrays.toString(operators));
            }
            return;
        }
        for (int i = MIN_OPERATOR_ID; i <= MAX_OPERATOR_ID; i++) {
            Operator operator = OPERATORS[i];
            operators[index] = operator;
            generateOperators(index + 1);
        }
    }

    public static void main(String[] args) {
        WebController webController = new WebController();
        while (true) {
            WebController.Puzzle puzzle = webController.randomPuzzle(14);
            System.out.println("Result from api is " + Arrays.toString(puzzle.getOperators()));
            BruteforceSolver solver = new BruteforceSolver(puzzle.getNumbers(), puzzle.getResult());
            solver.generateOperators(0);
        }
    }
}
