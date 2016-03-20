package com.lange.trader.struc;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lange on 19/3/16.
 */
public class PairTest {

    @Test
    public void testInstantiation() {
        Pair<String, Integer> pair = Pair.of("a", 1);
        assertThat(pair).isNotNull();
    }
}
