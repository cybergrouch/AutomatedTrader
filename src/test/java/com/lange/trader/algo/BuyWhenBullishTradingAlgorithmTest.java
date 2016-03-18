package com.lange.trader.algo;

import com.google.common.collect.Lists;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by lange on 18/3/16.
 */
@PrepareForTest({BuyWhenBullishTradingAlgorithm.class})
@RunWith(PowerMockRunner.class)
public class BuyWhenBullishTradingAlgorithmTest {

    private BuyWhenBullishTradingAlgorithm tradingAlgorithm;

    @Before
    public void setUp() {
        tradingAlgorithm = (BuyWhenBullishTradingAlgorithm) BuyWhenBullishTradingAlgorithm.create("a", "b");
    }

    @Test
    public void testInstantiation() {
        BuyWhenBullishTradingAlgorithm tradingAlgorithm = (BuyWhenBullishTradingAlgorithm) BuyWhenBullishTradingAlgorithm.create("a", "b", "c", "d");
        assertThat(tradingAlgorithm).isNotNull().isInstanceOf(BuyWhenBullishTradingAlgorithm.class);
    }

    @Test
    public void testIsProductTradable() {
        List<Pair<String, Boolean>> inputOutputExectations = Lists.newArrayList(Pair.of("a", true), Pair.of("c", false), Pair.of("d", false),  Pair.of("b", true));
        inputOutputExectations.forEach(p -> {
            boolean output = tradingAlgorithm.isProductTradable(p.getKey());
            assertEquals(String.format("Expected output for %s is %s but returned %s",
                        p.getKey(),
                        p.getValue(),
                        tradingAlgorithm.isProductTradable(p.getKey()),
                        output),
                    p.getValue(), output);
        });
    }

    @Test
    public void testBuildTrades_nonTradableProduct() {
        Price price = Price.create("d", 3.1415);
        assertPriceFeedsResultToTrade("Non Tradable Product returned Trade", null, Lists.newArrayList(price));
    }

    private void assertPriceFeedsResultToTrade(String tag, Trade expectedTrade, List<Price> inputPrices) {
        if (inputPrices == null || inputPrices.isEmpty()) {
            return;
        }
        List<Trade> trades = inputPrices.stream().map(p -> { return tradingAlgorithm.buildTrades(p); }).collect(Collectors.toList());
        Trade actualTrade = trades.get(trades.size() - 1);
        assertEquals(tag, expectedTrade, actualTrade);
    }


}
