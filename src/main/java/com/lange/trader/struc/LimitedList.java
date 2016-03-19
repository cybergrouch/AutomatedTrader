package com.lange.trader.struc;

import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by lange on 18/3/16.
 */
@Invariant({
        "maxSize > 0",
        "internalList == null || internalList.size() <= maxSize",
        "samplingSize <= maxSize / 2"
})
public class LimitedList<T> extends AbstractList<T> {

    public final int maxSize;
    public final int samplingSize;

    private final LinkedList<T> internalList;

    public static <T> LimitedList<T> create(int samplingSize) {
        return new LimitedList<>(samplingSize);
    }

    public static <T> LimitedList<T> create(int samplingSize, List<T> list) {
        LimitedList<T> limitedList = create(samplingSize);
        limitedList.addAll(list);
        return limitedList;
    }

    @Ensures({
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
            synchronized (this) {
                internalList.removeFirst();
            }
        }
    }

    public List<T> sample() {
        return Collections.unmodifiableList(internalList);
    }

    @Override
    public Object[] toArray() {
        return internalList.toArray();
    }

    @Override
    public boolean add(T t) {
        boolean success = false;
        synchronized (this) {
            success = internalList.add(t);
        }
        limitPopulation();
        return success;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean success = false;
        synchronized (this) {
            success = internalList.addAll(c);
        }
        limitPopulation();
        return success;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean success = false;
        synchronized (this) {
            success = internalList.addAll(index, c);
        }
        limitPopulation();
        return success;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean success = false;
        synchronized (this) {
            success = internalList.retainAll(c);
        }
        limitPopulation();
        return success;
    }

    @Override
    public boolean remove(Object o) {
        boolean success = false;
        synchronized (this) {
            success = internalList.remove(o);
        }
        return success;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean success = false;
        synchronized (this) {
            success = internalList.removeAll(c);
        }
        return success;
    }

    @Override
    public synchronized void sort(Comparator<? super T> c) {
        internalList.sort(c);
    }

    @Override
    public synchronized void clear() {
        internalList.clear();
    }

    @Override
    public synchronized T set(int index, T element) {
        return internalList.set(index, element);
    }

    @Override
    public synchronized void add(int index, T element) {
        internalList.add(index, element);
    }

    @Override
    public synchronized T remove(int index) {
        return internalList.remove(index);
    }

    @Override
    public synchronized boolean removeIf(Predicate<? super T> filter) {
        return internalList.removeIf(filter);
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
    public boolean containsAll(Collection<?> c) {
        return internalList.containsAll(c);
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

}
