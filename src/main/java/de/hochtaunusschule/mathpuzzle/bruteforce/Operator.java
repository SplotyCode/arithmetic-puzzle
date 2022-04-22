package de.hochtaunusschule.mathpuzzle.bruteforce;

import java.util.function.LongBinaryOperator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public enum Operator implements LongBinaryOperator {
    ADD('+', Long::sum),
    SUBTRACT('-', (left, right) -> left - right),
    MULTIPLY('*', (left, right) -> left * right),
    DIVIDE('/', (left, right) -> left / right);

    @Getter
    private final char symbol;
    private final LongBinaryOperator operation;

    public String symbolAsString() {
        return String.valueOf(symbol);
    }

    @Override
    public long applyAsLong(long left, long right) {
        return operation.applyAsLong(left, right);
    }
}
