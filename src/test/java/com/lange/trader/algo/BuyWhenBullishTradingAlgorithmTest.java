package com.lange.trader.algo;

import com.google.common.collect.Lists;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;
import com.lange.trader.struc.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by lange on 18/3/16.
 */
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
            boolean output = tradingAlgorithm.isProductTradable(p.key);
            assertEquals(String.format("Expected output for %s is %s but returned %s",
                        p.key,
                        p.value,
                        tradingAlgorithm.isProductTradable(p.key),
                        output),
                    p.value, output);
        });
    }

    @Test
    public void testBuildTrades_nonTradableProduct() {
        Price price = Price.create("d", 3.1415);
        assertPriceFeedsResultToTrade("Non Tradable Product returned Trade", Lists.newArrayList(Pair.of(price, null)));
    }

    @Test
    public void testBuildTrades_atLeastFourTradesToTrade_1() {
        List<Pair<Price, Trade>> priceFeed1 = Lists.newArrayList(Pair.of(Price.create("a", 3.1415), null));
        assertPriceFeedsResultToTrade("PriceFeed1", priceFeed1);
    }

    @Test
    public void testBuildTrades_atLeastFourTradesToTrade_2() {
        List<Pair<Price, Trade>> priceFeed2 = Lists.newArrayList(
                Pair.of(Price.create("a", 3.1415), null),
                Pair.of(Price.create("a", 4.1415), null));
        assertPriceFeedsResultToTrade("PriceFeed2", priceFeed2);
    }

    @Test
    public void testBuildTrades_atLeastFourTradesToTrade_3() {
        List<Pair<Price, Trade>> priceFeed3 = Lists.newArrayList(
                Pair.of(Price.create("a", 3.1415), null),
                Pair.of(Price.create("a", 4.1415), null),
                Pair.of(Price.create("a", 5.1415), null));
        assertPriceFeedsResultToTrade("PriceFeed3", priceFeed3);
    }

    @Test
    public void testBuildTrades_atLeastFourTradesToTrade_4() {
        List<Pair<Price, Trade>> priceFeed4 = Lists.newArrayList(
                Pair.of(Price.create("a", 3.1415), null),
                Pair.of(Price.create("a", 4.1415), null),
                Pair.of(Price.create("a", 5.1415), null),
                Pair.of(Price.create("a", 6.1415), Trade.create("a", Trade.Direction.BUY, 6.1415, 1000)));
        assertPriceFeedsResultToTrade("PriceFeed4", priceFeed4);
    }

    @Test
    public void testBuildTrades_nullTradeForNonTradableProduct() {
        List<Pair<Price, Trade>> priceFeed = Lists.newArrayList(
                Pair.of(Price.create("e", 3.1415), null),
                Pair.of(Price.create("e", 4.1415), null),
                Pair.of(Price.create("e", 5.1415), null),
                Pair.of(Price.create("e", 6.1415), null));
        assertPriceFeedsResultToTrade("Non Tradable PriceFeed", priceFeed);
    }

    @Test
    public void testAveragePrice() {
        List<Double> priceFeed = Lists.newArrayList(
                3.1415,
                4.1415,
                5.1415,
                6.1415);

        double averagePrice = tradingAlgorithm.averagePrice(priceFeed);
        assertEquals(Math.round(4.6415 * 10000), Math.round(averagePrice * 10000));

    }

    private void assertPriceFeedsResultToTrade(String tag, List<Pair<Price, Trade>> inputOutputExpectations) {
        if (inputOutputExpectations == null || inputOutputExpectations.isEmpty()) {
            return;
        }

        for (int i = 0; i < inputOutputExpectations.size(); i++) {
            Pair<Price, Trade> inputOutputExpectation = inputOutputExpectations.get(i);
            Trade actualTrade = tradingAlgorithm.buildTrades(inputOutputExpectation.key);
            assertEquals(String.format("[%s #%s] Mismatch", tag, i), inputOutputExpectation.value, actualTrade);
        }
    }


}
