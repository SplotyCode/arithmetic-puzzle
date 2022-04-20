package de.hochtaunusschule.mathpuzzle.math;

import com.google.common.collect.Multiset;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author David (_Esel)
 */
@Getter
@ToString(exclude = "allResults")
@Accessors(fluent = true)
@RequiredArgsConstructor
public class Expression {
    private final long[] numbers;
    private final Operator[] operators;
    private final long result;
    private final Multiset<Long> allResults;

    public long duplicates() {
        return allResults.count(result);
    }
}
