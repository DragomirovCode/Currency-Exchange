package ru.dragomirov.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CalculationDTO {
    @SerializedName("baseCurrency")
    private CurrencyDTO baseCurrency;
    @SerializedName("targetCurrency")
    private CurrencyDTO targetCurrency;
    @SerializedName("rate")
    private BigDecimal rate;
    @SerializedName("amount")
    private BigDecimal amount;
    @SerializedName("convertedAmount")
    private BigDecimal convertedAmount;

    public CalculationDTO(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency,
                          BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyDTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyDTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyDTO getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyDTO targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
