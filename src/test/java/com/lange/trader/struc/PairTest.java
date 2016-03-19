package com.lange.trader.struc;

import com.google.java.contract.PreconditionError;
import com.lange.trader.model.Price;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
