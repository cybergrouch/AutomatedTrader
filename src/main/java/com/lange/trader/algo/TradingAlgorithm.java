package com.lange.trader.algo;

import com.google.java.contract.Invariant;
import com.lange.trader.model.Price;
import com.lange.trader.model.Trade;

/**
 * Created by lange on 17/3/16.
 */
@Invariant({"true"})
public interface TradingAlgorithm {
    /**
     * Builds a trade to be executed based on the supplied prices.
     * @param price data
     * @return trade to execute
     */
    Trade buildTrades(Price price);
}