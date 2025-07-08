package problems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        Map<String, Integer> map = new HashMap<>();
        for (String key : list) {
            map.put(key, map.getOrDefault(key, 0) + 1);
        }
        return map.containsValue(3);
    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> map = new HashMap<>();
        for (String key1 : m1.keySet()) {
            if (m2.containsKey(key1) && m2.containsValue(m1.get(key1))) {
                map.put(key1, m1.get(key1));
            }
        }
        return map;
    }
}
