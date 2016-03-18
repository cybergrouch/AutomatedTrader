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
        "priceValue >= 0",
        "quantity >= 0"
})
public class Trade {

    enum Direction {
        BUY,
        SELL;
    }

    public final String productName;
    public final Direction tradeDirection;
    public final double priceValue;
    public final int quantity;

    @Requires({
            "productName != null",
            "tradeDirection != null",
            "priceValue >= 0",
            "quantity >= 0"
    })
    private Trade(String productName, Direction tradeDirection, double priceValue, int quantity) {
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
        return new Trade(productName, tradeDirection, priceValue, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trade)) return false;

        Trade trade = (Trade) o;

        if (Double.compare(trade.priceValue, priceValue) != 0) return false;
        if (quantity != trade.quantity) return false;
        if (!productName.equals(trade.productName)) return false;
        return tradeDirection == trade.tradeDirection;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = productName.hashCode();
        result = 31 * result + tradeDirection.hashCode();
        temp = Double.doubleToLongBits(priceValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Trade{ productName: %s, tradeDirection: %s, priceValue: %s, quantity: %s }", productName,
                tradeDirection.name(), priceValue, quantity);
    }
}
