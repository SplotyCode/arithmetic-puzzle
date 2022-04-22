package de.hochtaunusschule.mathpuzzle.api;

import de.hochtaunusschule.mathpuzzle.bruteforce.Operator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@Getter
@RequiredArgsConstructor
public class Expression {
    private final long result;
    private final long[] numbers;
    private final Operator[] operators;
}
