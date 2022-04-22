package de.hochtaunusschule.mathpuzzle.view;

import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import de.hochtaunusschule.mathpuzzle.api.Expression;
import de.hochtaunusschule.mathpuzzle.generate.ExpressionGenerator;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author David (_Esel)
 */
@RestController
public class WebController {
    @Getter
    @Accessors(fluent = false)
    @RequiredArgsConstructor
    public static class Puzzle {
        private final long[] numbers;
        private final String[] operators;
        private final long result;
    }

    /*private Expression expression(int operands) {
        int numbers = operands + 1;
        if (operands <= 7) {
            ExpressionGenerator left = new ExpressionGenerator(numbers);
            return left.generate().pickAny();
        }
        int leftPlaces = numbers / 2;
        int rightPlaces = leftPlaces + numbers % 2;
        ExpressionGenerator left = new ExpressionGenerator(leftPlaces);
        ExpressionGenerator right = new ExpressionGenerator(rightPlaces);
        return ExpressionCombination.combine(left, right);
    }*/

    @GetMapping(value = "/api/puzzle", produces = MediaType.APPLICATION_JSON_VALUE)
    public Puzzle randomPuzzle(@RequestParam("operands") int operands) {
        long took = System.currentTimeMillis();
        //Expression expression = Generator.generate(operands);
        ExpressionGenerator generator = new ExpressionGenerator(operands);
        Expression expression = generator.generate().pickAny();
        System.out.println("Took: " + (System.currentTimeMillis() - took));
        return new Puzzle(
            expression.numbers(),
            Arrays.stream(expression.operators()).map(Operator::symbolAsString).toArray(String[]::new),
            expression.result()
        );
    }
}
