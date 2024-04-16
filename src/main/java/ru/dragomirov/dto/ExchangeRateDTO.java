package ru.dragomirov.dto;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;
public class ExchangeRateDTO {
    @SerializedName("id")
    private int id;
    @SerializedName("baseCurrencyId")
    private CurrencyDTO baseCurrencyId;
    @SerializedName("targetCurrencyId")
    private CurrencyDTO targetCurrencyId;
    @SerializedName("rate")
    private BigDecimal rate;

    public ExchangeRateDTO(CurrencyDTO baseCurrencyId, CurrencyDTO targetCurrencyId, BigDecimal rate) {
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

    public CurrencyDTO getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(CurrencyDTO baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public CurrencyDTO getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(CurrencyDTO targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
