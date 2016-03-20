package com.lange.trader.struc;

/**
 * Created by lange on 19/3/16.
 */
public class Pair<K, V> {

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public final K key;
    public final V value;

    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
