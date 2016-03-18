package com.lange.trader.struc;

import com.google.common.collect.Maps;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by lange on 18/3/16.
 */
@Invariant({
        "samplingSize > 0",
        "cache != null"
})
public class MultiMap<K extends Comparable<K>, V> {

    private final ConcurrentMap<K, LimitedList<V>> cache = Maps.newConcurrentMap();
    private final int listSamplingSize;

    @Requires({
            "samplingSize > 0"
    })
    public static <K extends Comparable<K>, V> MultiMap<K, V> create(int samplingSize) {
        return new MultiMap<>(samplingSize);
    }

    @Requires({
            "samplingSize > 0"
    })
    private MultiMap(int listSamplingSize) {
        this.listSamplingSize = listSamplingSize;
    }

    @Requires({
            "k != null"
    })
    public List<V> get(K k) {
        return cache.get(k).sample();
    }

    @Requires({
            "k != null"
    })
    public List<V> remove(K k) {
        return cache.remove(k).sample();
    }

    @Requires({
            "k != null",
            "v != null"
    })
    public void put(K k, V v) {
        cache.putIfAbsent(k, LimitedList.create(listSamplingSize));
        cache.get(k).add(v);
    }

    @Requires({
            "k != null",
            "v != null"
    })
    public boolean remove(K k, V v) {
        LimitedList<V> list = cache.get(k);
        if (list == null || list.isEmpty()) {
            return false;
        } else {
            return list.remove(v);
        }
    }

}
