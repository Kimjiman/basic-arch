package com.basicarch.base.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T> void ifEmpty(Collection<T> collection, Consumer<Collection<T>> action) {
        if (isEmpty(collection)) action.accept(collection);
    }

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return isEmpty(collection) ? Stream.empty() : collection.stream();
    }

    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T>... collections) {
        if (collections == null || collections.length == 0) return new ArrayList<>();

        List<T> mergedCollection = new ArrayList<>();
        for (Collection<T> it : collections) {
            if (it != null) mergedCollection.addAll(it);
        }
        return mergedCollection;
    }

    public static <T> List<T> splice(Collection<T> collection, int start, int end) {
        if (collection == null || collection.isEmpty()) return new ArrayList<>();
        return collection.stream()
                .skip(start)
                .limit((long) end - start)
                .collect(Collectors.toList());
    }

    public static <T> List<Collection<T>> separationList(Collection<T> collection, int size) {
        if (collection == null || collection.isEmpty()) return new ArrayList<>();
        List<Collection<T>> ret = new ArrayList<>();
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            List<T> subList = new ArrayList<>();
            for (int i = 0; i < size && iterator.hasNext(); i++) {
                subList.add(iterator.next());
            }
            ret.add(subList);
        }
        return ret;
    }

    @SafeVarargs
    public static <T> List<List<T>> separationList(Collection<T> collection, Predicate<T>... predicates) {
        if (collection == null || collection.isEmpty()) return Collections.emptyList();
        List<List<T>> result = Arrays.stream(predicates)
                .map(p -> new ArrayList<T>())
                .collect(Collectors.toList());

        for (T item : collection) {
            for (int i = 0; i < predicates.length; i++) {
                if (predicates[i].test(item)) {
                    result.get(i).add(item);
                }
            }
        }
        return result;
    }

    // Integer[] array = listToArray(list, Integer[]::new);
    public static <T> T[] listToArray(List<T> list, IntFunction<T[]> generator) {
        return list.toArray(generator.apply(list.size()));
    }

    public static <T> List<T> arrayToList(T[] arr) {
        return Arrays.asList(arr);
    }

    public static <K, V> Map<K, V> toMap(Collection<V> collection, Function<? super V, ? extends K> keyMapper) {
        if (isEmpty(collection)) return Collections.emptyMap();
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity(), (existing, replacement) -> existing));
    }

    public static <T, R> List<R> extractList(Collection<T> collection, Function<? super T, ? extends R> mapper) {
        return safeStream(collection).map(mapper).collect(Collectors.toList());
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        if (isEmpty(list)) return list;
        return new ArrayList<>(new LinkedHashSet<>(list));
    }
}
