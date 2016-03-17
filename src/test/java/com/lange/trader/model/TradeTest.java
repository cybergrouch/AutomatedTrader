package com.lange.trader.model;

import com.google.java.contract.PreconditionError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by lange on 17/3/16.
 */
@PrepareForTest({Trade.class})
@RunWith(PowerMockRunner.class)
public class TradeTest {

    @Test
    public void testInstantiation() {
        Trade trade1 = Trade.create("Item A", Trade.Direction.BUY, 3.1415, 100);
        assertThat(trade1).isNotNull();

        Trade trade2 = Trade.create("Item B", Trade.Direction.SELL, new BigDecimal(1.412), 153);
        assertThat(trade2).isNotNull();
    }

    @Test
    public void testInstantiationFailure_Null_ProductName() {
        assertThatThrownBy(() ->  { Trade.create(null, Trade.Direction.BUY, 3.1415, 100); })
                .isInstanceOf(PreconditionError.class)
                .hasMessage("productName != null")
                .hasNoCause();
    }

    @Test
    public void testInstantiationFailure_Null_TradeDirection() {
        assertThatThrownBy(() ->  { Trade.create("abc", null, 3.1415, 100); })
                .isInstanceOf(PreconditionError.class)
                .hasMessage("tradeDirection != null")
                .hasNoCause();
    }

    @Test
    public void testInstantiationFailure_Negative_priceValue() {
        assertThatThrownBy(() ->  { Trade.create("abc", Trade.Direction.BUY, -3.1415, 100); })
                .isInstanceOf(PreconditionError.class)
                .hasMessage("priceValue >= 0")
                .hasNoCause();
    }

    @Test
    public void testInstantiationFailure_Negative_quantity() {
        assertThatThrownBy(() ->  { Trade.create("abc", Trade.Direction.BUY, 3.1415, -100); })
                .isInstanceOf(PreconditionError.class)
                .hasMessage("quantity >= 0")
                .hasNoCause();
    }

}
