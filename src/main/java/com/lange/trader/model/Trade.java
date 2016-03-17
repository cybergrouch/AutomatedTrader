package com.lange.trader.model;

import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

import java.math.BigDecimal;

/**
 * Created by lange on 17/3/16.
 */
@Invariant({
        "productName != null",
        "tradeDirection != null",
        "priceValue != null",
        "priceValue.doubleValue() >= 0",
        "quantity >= 0"
})
public class Trade {

    enum Direction {
        BUY,
        SELL;
    }

    public final String productName;
    public final Direction tradeDirection;
    public final BigDecimal priceValue;
    public final int quantity;

    @Requires({
            "productName != null",
            "tradeDirection != null",
            "priceValue != null",
            "priceValue.doubleValue() >= 0",
            "quantity >= 0"
    })
    private Trade(String productName, Direction tradeDirection, BigDecimal priceValue, int quantity) {
        this.productName = productName;
        this.tradeDirection = tradeDirection;
        this.priceValue = priceValue;
        this.quantity = quantity;
    }

    @Requires({
            "productName != null",
            "tradeDirection != null",
            "priceValue >= 0",
            "quantity >= 0"
    })
    public static Trade create(String productName, Direction tradeDirection, double priceValue, int quantity) {
        return create(productName, tradeDirection, new BigDecimal(priceValue), quantity);
    }

    @Requires({
            "productName != null",
            "tradeDirection != null",
            "priceValue != null",
            "priceValue.doubleValue() >= 0",
            "quantity >= 0"
    })
    public static Trade create(String productName, Direction tradeDirection, BigDecimal priceValue, int quantity) {
        return new Trade(productName, tradeDirection, priceValue, quantity);
    }


    @Override
    public String toString() {
        return "Trade{ productName: %s, tradeDirection: %s, priceValue: %s, quantity: %s }".format(productName,
                tradeDirection.name(), priceValue.doubleValue(), quantity);
    }
}
