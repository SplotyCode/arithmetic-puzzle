package de.hochtaunusschule.mathpuzzle.bruteforce;

import de.hochtaunusschule.mathpuzzle.view.WebController;
import java.util.Arrays;

/**
 * @author David (_Esel)
 */
public class BruteforceSolver {
    private static final Operator[] OPERATORS = new Operator[] {Operator.ADD, Operator.SUBTRACT, Operator.MULTIPLY, Operator.DIVIDE};
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
        for (Operator operator : OPERATORS) {
            operators[index] = operator;
            generateOperators(index + 1);
        }
    }

    public static void main(String[] args) {
        WebController webController = new WebController();
       // while (true) {
        //    WebController.Puzzle puzzle = wgebController.randomPuzzle(15);
        //          //  System.out.println("Result from api is " + Arrays.toStrin(puzzle.getOperators()) + " " + Arrays.toString(puzzle.getNumbers()) + " " + puzzle.getResult());
           /* de.hochtaunusschule.mathpuzzle.generate.ExpressionGenerator
                generator = new ExpressionGenerator(15);
            long generate = System.currentTimeMillis();
            Expression expression = generator.generate().pickAny();
            long ended = System.currentTimeMillis();
            System.out.println("Generated in " + (ended - generate));
            System.out.println("testing " + Arrays.toString(expression.numbers()) + " " + expression.result() + " " +
                Arrays.toString(expression.operators()));*/
            BruteforceSolver solver = new BruteforceSolver(new long[] {9,1,7,4,5,9,9,5,3,3,3,4,1,7,6,1,5,1,1}, 43641);
            solver.generateOperators(0);
            //System.out.println("tested in " + (System.currentTimeMillis() - ended));
      //  }
    }
}
