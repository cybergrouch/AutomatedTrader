package com.lange.trader.algo;

import com.google.java.contract.Requires;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;
import com.lange.trader.struc.MultiMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lange on 18/3/16.
 */
public class BuyWhenBullishTradingAlgorithm implements TradingAlgorithm {

    @Requires({
            "productNames != null",
            "productNames.length > 0"
    })
    public static TradingAlgorithm create(String... productNames) {
        return new BuyWhenBullishTradingAlgorithm(productNames);
    }

    @Requires({
            "productNamesList != null",
            "productNamesList.size() > 0"
    })
    public static TradingAlgorithm create(List<String> productNamesList) {
        String[] productNames = productNamesList.toArray(new String[productNamesList.size()]);
        return new BuyWhenBullishTradingAlgorithm(productNames);
    }

    private final List<String> productNames;
    private final MultiMap<String, Double> priceFeeds;

    public BuyWhenBullishTradingAlgorithm(String[] productNames) {
        this.productNames = Arrays.asList(productNames);
        this.priceFeeds = MultiMap.create(4);
    }

    @Override
    @Requires({
            "price != null"
    })
    public Trade buildTrades(Price price) {
        if (!isProductTradable(price.productName)) {
            return null;
        }

        priceFeeds.put(price.productName, price.priceValue);
        List<Double> prices = priceFeeds.get(price.productName);

        if (prices.size() < 4) {
            return null;
        }


        boolean isTrade = prices.get(0) < averagePrice(prices);
        if (!isTrade) {
            return null;
        }

        return Trade.create(price.productName, Trade.Direction.BUY, prices.get(3), 1000);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("BuyWhenBullishTradingAlgorithm{" +
                "productNames=[");
        buffer.append(productNames.stream().collect(Collectors.joining(","))).append("]}");
        return buffer.toString();
    }

    boolean isProductTradable(String productName) {
        return productNames.contains(productName);
    }

    double averagePrice(List<Double> prices) {
        return prices.stream().reduce((acc, value) -> acc + value).orElse(0.0) / prices.size();
    }
}
