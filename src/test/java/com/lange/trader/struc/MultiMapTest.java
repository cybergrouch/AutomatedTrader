package com.lange.trader.struc;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lange on 18/3/16.
 */
public class MultiMapTest {

    @Test
    public void testInstantiation() {
        MultiMap<String, Integer> multiMap1 = MultiMap.create(4);
        assertThat(multiMap1).isNotNull();
    }

    @Test
    public void testSampling() {
        MultiMap<String, Integer> multiMap1 = MultiMap.create(4);
        multiMap1.put("a", 1);
        multiMap1.put("b", 1);
        multiMap1.put("a", 3);
        multiMap1.put("b", 3);
        multiMap1.put("a", 5);
        multiMap1.put("b", 5);
        multiMap1.put("a", 7);
        multiMap1.put("b", 7);
        multiMap1.put("a", 9);
        multiMap1.put("b", 9);

        List<Integer> aList = multiMap1.get("a");
        assertThat(aList).isNotNull().containsExactly(3, 5, 7, 9).isNotInstanceOf(LimitedList.class);

        List<Integer> bList = multiMap1.get("b");
        assertThat(bList).isNotNull().containsExactly(3, 5, 7, 9).isNotInstanceOf(LimitedList.class);
    }
}
