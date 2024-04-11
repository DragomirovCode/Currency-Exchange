package com.example.dto;

import java.math.BigDecimal;
import com.google.gson.annotations.SerializedName;
public class ExchangeRateDTO {
    @SerializedName("id")
    private int id;
    @SerializedName("baseCurrencyId")
    private int baseCurrencyId;
    @SerializedName("targetCurrencyId")
    private int targetCurrencyId;
    @SerializedName("rate")
    private BigDecimal rate;

    public ExchangeRateDTO(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
