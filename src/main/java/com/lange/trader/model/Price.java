package com.lange.trader.model;

import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

import java.math.BigDecimal;

/**
 * Created by lange on 17/3/16.
 */
@Invariant({
        "productName != null",
        "priceValue >= 0"
})
public class Price {

    public final String productName;
    public final double priceValue;

    @Requires({
            "productName != null",
            "priceValue >= 0"
    })
    private Price(String productName, double priceValue) {
        this.productName = productName;
        this.priceValue = priceValue;
    }

    @Requires({
            "productName != null",
            "priceValue >= 0"
    })
    public static Price create(String productName, double priceValue) {
        return new Price(productName, priceValue);
    }


    @Override
    public String toString() {
        return String.format("Price{ productName: %s, priceValue: %s }", productName, priceValue);
    }
}