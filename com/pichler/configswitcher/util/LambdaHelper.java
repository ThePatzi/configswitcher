package com.pichler.configswitcher.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Patrick on 18.05.2016.
 */
public class LambdaHelper {
    public static <I, T> Predicate<I> distinct(Function<I, T> mapper) {
        Map<T, Boolean> map = new HashMap<>();

        return obj -> {
            T key = mapper.apply(obj);

            if (!map.containsKey(key)) {
                map.put(key, true);
                return true;
            }
            return false;
        };
    }
}
