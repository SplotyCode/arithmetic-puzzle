package de.hochtaunusschule.mathpuzzle.versuch2;

import de.hochtaunusschule.mathpuzzle.math.Operator;
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
