package com.mcmoddev.bot.util;

import com.google.common.collect.Lists;

import java.util.*;

public class Utilities {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue (Map<K, V> map, boolean invert) {

        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        if (invert)
            list = Lists.reverse(list);

        final Map<K, V> result = new LinkedHashMap<>();
        for (final Map.Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }
}