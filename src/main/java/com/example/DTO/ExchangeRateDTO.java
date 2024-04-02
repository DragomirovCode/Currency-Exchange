package com.example.DTO;

import java.math.BigDecimal;

public class ExchangeRateDTO {
    private int ID;
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private BigDecimal Rate;

    public ExchangeRateDTO(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        Rate = rate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBaseCurrencyId() {
        return BaseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        BaseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return TargetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        TargetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return Rate;
    }

    public void setRate(BigDecimal rate) {
        Rate = rate;
    }
}
