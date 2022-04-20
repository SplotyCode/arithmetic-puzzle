package de.hochtaunusschule.mathpuzzle.view;

import de.hochtaunusschule.mathpuzzle.math.Operator;
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
    static class Puzzle {
        private final int[] numbers;
        private final String[] operators;
        private final int result;
    }

    @GetMapping(value = "/api/puzzle", produces = MediaType.APPLICATION_JSON_VALUE)
    public Puzzle randomPuzzle(@RequestParam("operands") int operands) {
        int[] numbers = new int[operands + 1];
        String[] operators = new String[operands];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = (int) (Math.random() * 10);
        }
        for (int i = 0; i < operands; i++) {
            operators[i] = Operator.values()[(int) (Math.random() * 4)].symbolAsString();
        }
        return new Puzzle(numbers, operators, (int) (Math.random() * 100));
    }
}
