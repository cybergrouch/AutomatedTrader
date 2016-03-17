package com.lange.trader.struc;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by lange on 18/3/16.
 */
@Invariant({
        "maxSize > 0",
        "internalList == null || internalList.size() <= maxSize",
        "samplingSize <= maxSize / 2"
})
public class LimitedList<T> implements List<T> {

    public final int maxSize;
    public final int samplingSize;

    private final LinkedList<T> internalList;

    public static <T> LimitedList<T> create(int samplingSize) {
        return new LimitedList<>(samplingSize);
    }

    @Requires({
            "maxSize > 0",
            "2 * samplingSize <= maxSize"
    })
    private LimitedList(int samplingSize) {
        this.samplingSize = samplingSize;
        this.maxSize = 2 * samplingSize;
        this.internalList = new LinkedList<>();
    }

    private void limitPopulation() {
        while (internalList.size() > samplingSize) {
            internalList.removeFirst();
        }
    }

    public List<T> sample() {
        limitPopulation();
        return Lists.newArrayList(internalList);
    }

    @Override
    public Object[] toArray() {
        limitPopulation();
        return internalList.toArray();
    }

    @Override
    public boolean add(T t) {
        boolean success = internalList.add(t);
        limitPopulation();
        return success;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean success = internalList.addAll(c);
        limitPopulation();
        return success;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean success = internalList.addAll(index, c);
        limitPopulation();
        return success;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean success = internalList.retainAll(c);
        limitPopulation();
        return success;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        internalList.replaceAll(operator);
        limitPopulation();
    }

    public Iterator<T> descendingIterator() {
        return internalList.descendingIterator();
    }


    /** DELEGATED METHOD CALLS **/

    @Override
    public int size() {
        return internalList.size();
    }

    @Override
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internalList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return internalList.iterator();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return internalList.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return internalList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return internalList.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return internalList.removeAll(c);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        internalList.sort(c);
    }

    @Override
    public void clear() {
        internalList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return internalList.equals(o);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public T get(int index) {
        return internalList.get(index);
    }

    @Override
    public T set(int index, T element) {
        return internalList.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        internalList.add(index, element);
    }

    @Override
    public T remove(int index) {
        return internalList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return internalList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return internalList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return internalList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return internalList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return internalList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return internalList.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return internalList.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return internalList.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return internalList.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        internalList.forEach(action);
    }
}
