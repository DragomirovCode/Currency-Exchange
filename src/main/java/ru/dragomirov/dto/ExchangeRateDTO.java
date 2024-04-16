package ru.dragomirov.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
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
}
