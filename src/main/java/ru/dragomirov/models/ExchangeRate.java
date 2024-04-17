package ru.dragomirov.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRate {
    @SerializedName("id")
    private int id;

    @SerializedName("baseCurrencyId")
    private Currency baseCurrencyId;

    @SerializedName("targetCurrencyId")
    private Currency targetCurrencyId;

    @SerializedName("rate")
    private BigDecimal rate;

    public ExchangeRate(Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }
}
