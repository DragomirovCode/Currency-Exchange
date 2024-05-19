package ru.dragomirov.dto;

import ru.dragomirov.models.Currency;

import java.math.BigDecimal;

public class CalculationDTOFactory {
    public CalculationDTO createCalculationDTO(Currency baseCurrency, Currency targetCurrency,
                                               BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        CalculationDTO calculationDTO = new CalculationDTO();
        calculationDTO.setBaseCurrency(baseCurrency);
        calculationDTO.setTargetCurrency(targetCurrency);
        calculationDTO.setRate(rate);
        calculationDTO.setAmount(amount);
        calculationDTO.setConvertedAmount(convertedAmount);
        return calculationDTO;
    }
}
