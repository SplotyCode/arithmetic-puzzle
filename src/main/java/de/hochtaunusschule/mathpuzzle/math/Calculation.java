package de.hochtaunusschule.mathpuzzle.math;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

@AllArgsConstructor
public class Calculation {
    private long[] numbers;
    private Operator[] operators;

    public boolean calculate() {
        if (multiDivision()) {
            return true;
        }
        additionSubtraction();
        if (numbers.length != 1) {
            throw new IllegalStateException("Finished with: "
                + Arrays.toString(numbers) + " "
                + Arrays.toString(operators));
        }
        return false;
    }

    public long result() {
        if (numbers.length != 1) {
            throw new IllegalStateException();
        }
        return numbers[0];
    }

    private boolean runOperation(Operator operator, int index) {
        long left = numbers[index];
        long right = numbers[index + 1];
        if (operator == Operator.DIVIDE && validateDivide(left, right)) {
            return true;
        }
        numbers[index] = operator.applyAsLong(left, right);
        numbers = ArrayUtils.remove(numbers, index + 1);
        operators = ArrayUtils.remove(operators, index);
        return false;
    }

    private boolean multiDivision() {
        for (int i = 0; i < operators.length; i++) {
            Operator operator = operators[i];
            if (operator == Operator.MULTIPLY || operator == Operator.DIVIDE) {
               if (runOperation(operator, i)) {
                   return true;
               }
               i--;
            }
        }
        return false;
    }

    private boolean validateDivide(long left, long right) {
        return right == 0 || left % right != 0;
    }

    private void additionSubtraction() {
        while (operators.length != 0) {
            runOperation(operators[0], 0);
        }
    }
}
