package com.lange.trader.struc;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lange on 18/3/16.
 */
public class LimitedListTest {

    @Test
    public void testInstantiation() {
        LimitedList<Integer> listOfIntegers = LimitedList.create(2);
        assertThat(listOfIntegers).isNotNull();
        assertThat(listOfIntegers.maxSize).isEqualTo(4);
    }

    @Test
    public void testAddUpToLimit() {
        LimitedList<Integer> listOfIntegers = LimitedList.create(2);
        assertThat(listOfIntegers).isNotNull();
        assertThat(listOfIntegers.maxSize).isEqualTo(4);
        assertThat(listOfIntegers.samplingSize).isEqualTo(2);

        listOfIntegers.add(1);
        assertThat(listOfIntegers).containsExactly(1);
        listOfIntegers.add(2);
        assertThat(listOfIntegers).containsExactly(1, 2);
        listOfIntegers.add(3);
        assertThat(listOfIntegers).containsExactly(2, 3);
        listOfIntegers.add(4);
        assertThat(listOfIntegers).containsExactly(3, 4);
        listOfIntegers.add(5);
        assertThat(listOfIntegers).containsExactly(4, 5);
    }

}
