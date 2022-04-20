package de.hochtaunusschule.mathpuzzle.versuch2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import lombok.Getter;

/**
 * @author David (_Esel)
 */
public class DuplicateTracker<T> {
    private static final Random random = new Random();

    private final Set<Long> duplicatedResults = new HashSet<>();
    @Getter
    private final Map<Long, T> results = new HashMap<>();

    public Map.Entry<Long, T> pickAny() {
        List<Map.Entry<Long, T>> allResults = new ArrayList<>(results.entrySet());
        return allResults.get(random.nextInt(allResults.size()));
    }

    public void track(long result, T value) {
        if (duplicatedResults.contains(result)) {
            return;
        }
        if (results.put(result, value) != null) {
            results.remove(result);
            duplicatedResults.add(result);
        }
    }

    public void invalidate(long possibleResult) {
        results.remove(possibleResult);
    }
}
