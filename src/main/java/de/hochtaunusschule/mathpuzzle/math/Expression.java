package de.hochtaunusschule.mathpuzzle.math;

import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class Expression {
    private final long[] numbers;
    private final Operator[] operators;
    private final Set<Long> allResults;
}
