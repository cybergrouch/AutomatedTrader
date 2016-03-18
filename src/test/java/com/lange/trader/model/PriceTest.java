package com.lange.trader.model;

import com.google.java.contract.PreconditionError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by lange on 17/3/16.
 */
@PrepareForTest({Price.class})
@RunWith(PowerMockRunner.class)
public class PriceTest {

    @Test
    public void testInstantiation() {
        Price price1 = Price.create("Item A", 3.1415);
        assertThat(price1).isNotNull();

        Price price2 = Price.create("Item B", 1.412);
        assertThat(price2).isNotNull();
    }

    @Test
    public void testInstantiationFailure_Null_ProductName() {
        assertThatThrownBy(() ->  { Price.create(null, 3.1415); })
                .isInstanceOf(PreconditionError.class)
                .hasMessage("productName != null")
                .hasNoCause();
    }

    @Test
    public void testInstantiationFailure_Negative_priceValue() {
        assertThatThrownBy(() ->  { Price.create("abc", -3.1415); })
                .isInstanceOf(PreconditionError.class)
                .hasMessage("priceValue >= 0")
                .hasNoCause();
    }

}
