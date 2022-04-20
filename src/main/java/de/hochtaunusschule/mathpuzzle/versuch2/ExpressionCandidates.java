package de.hochtaunusschule.mathpuzzle.versuch2;

import de.hochtaunusschule.mathpuzzle.math.Operator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author David (_Esel)
 */
@RequiredArgsConstructor
public class ExpressionCandidates {
    private static final Random random = new Random();

    @Getter
    private final long[] numbers;
    @Getter
    private final Map<Long, Operator[]> uniqueResults;

    public Expression pickAny() {
        List<Map.Entry<Long, Operator[]>> allResults = new ArrayList<>(uniqueResults.entrySet());
        Map.Entry<Long, Operator[]> result = allResults.get(random.nextInt(allResults.size()));
        return new Expression(result.getKey(), numbers, result.getValue());
    }
}
