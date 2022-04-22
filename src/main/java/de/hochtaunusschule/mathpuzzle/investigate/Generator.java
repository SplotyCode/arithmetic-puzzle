package de.hochtaunusschule.mathpuzzle.investigate;

import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import de.hochtaunusschule.mathpuzzle.api.Expression;
import java.util.Arrays;
import java.util.Random;

/**
 * @author David (_Esel)
 */
public class Generator {
    private static final Random random = new Random();

   public static Expression generate(int operands) {
       long[] number = new long[operands + 1];
       Operator[] operators = new Operator[operands];
       Arrays.fill(operators, Operator.MULTIPLY);
       long result = 1;
       for (int i = 0; i < number.length; i++) {
           int num = random.nextInt(7) + 3;
           number[i] = num;
       }
       /*long result = 1;
       do {
           for (int i = 0; i < number.length / 2; i++) {
               TupleGen.Multiplication multiplication = TupleGen.random();
               number[i * 2] = multiplication.left;
               number[i * 2 + 1] = multiplication.right;
               result *= multiplication.result;
           }
       } while (result <= 0);*/
       return new Expression(result, number, operators);
   }
}
