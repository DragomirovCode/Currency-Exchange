package ru.dragomirov.dto;

import lombok.Getter;
import lombok.Setter;
import ru.dragomirov.entities.Currency;

import java.math.BigDecimal;

@Getter
@Setter
public class CalculationDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
