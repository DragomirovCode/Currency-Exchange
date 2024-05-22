package ru.dragomirov.dto;

import java.math.BigDecimal;

public class CalculationDTOFactory {
    public CalculationDTO createCalculationDTO(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency,
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
