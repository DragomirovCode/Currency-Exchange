package ru.dragomirov.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
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
}
