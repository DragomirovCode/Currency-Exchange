package ru.dragomirov.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Calculation {
    @SerializedName("baseCurrency")
    private Currency baseCurrency;

    @SerializedName("targetCurrency")
    private Currency targetCurrency;

    @SerializedName("rate")
    private BigDecimal rate;

    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("convertedAmount")
    private BigDecimal convertedAmount;

    public Calculation(Currency baseCurrency, Currency targetCurrency,
                       BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
