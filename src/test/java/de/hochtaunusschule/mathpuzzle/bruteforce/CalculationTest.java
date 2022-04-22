package de.hochtaunusschule.mathpuzzle.bruteforce;

import org.junit.jupiter.api.Test;

/**
 * @author David (_Esel)
 */
class CalculationTest {
    @Test
    void testA() {
        System.out.println(calc(Operator.ADD, Operator.ADD, Operator.ADD));
        System.out.println(calc(Operator.ADD, Operator.SUBTRACT, Operator.ADD));
        System.out.println(calc(Operator.SUBTRACT, Operator.ADD, Operator.ADD));
        System.out.println(calc(Operator.MULTIPLY, Operator.MULTIPLY, Operator.DIVIDE));
        System.out.println(calc(Operator.MULTIPLY, Operator.MULTIPLY, Operator.MULTIPLY));
    }

    private long calc(Operator... operators) {
        Calculation calculation = new Calculation(new long[] {2, 4, 5, 6}, operators);
        if (calculation.calculate()) {
            return -1;
        }
        return calculation.result();
    }
}