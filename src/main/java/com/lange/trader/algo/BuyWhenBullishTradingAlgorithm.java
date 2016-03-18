package com.lange.trader.algo;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.java.contract.Requires;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lange on 18/3/16.
 */
public class BuyWhenBullishTradingAlgorithm implements TradingAlgorithm {

    @Requires({
            "productNames != null",
            "productNames.length > 0",
            "Iterables.all(Arrays.asList(productNames), x -> { return x != null && x.trim() != \"\"; })"
    })
    public static TradingAlgorithm create(String... productNames) {
        return new BuyWhenBullishTradingAlgorithm(productNames);
    }

    private final List<String> productNames;

    public BuyWhenBullishTradingAlgorithm(String[] productNames) {
        this.productNames = Arrays.asList(productNames);
    }

    @Override
    @Requires({
            "price != null"
    })
    public Trade buildTrades(Price price) {
        return null;
    }

    boolean isProductTradable(String productName) {
        return productNames.contains(productName);
    }
}
