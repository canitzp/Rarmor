package de.canitzp.rarmor.util;

import java.util.*;

/**
 * @author canitzp
 */
public class JavaUtil{

    @SafeVarargs
    public static <E> List<E> newList(E... elements){
        if (elements != null){
            List<E> list = new ArrayList<>();
            Collections.addAll(list, elements);
            return list;
        }
        return new ArrayList<>();
    }

    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByKeys(Map<K,V> map){
        List<K> keys = new LinkedList<>(map.keySet());
        Collections.sort(keys);
        Map<K,V> sortedMap = new LinkedHashMap<>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }

}
