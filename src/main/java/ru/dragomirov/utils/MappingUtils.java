package ru.dragomirov.utils;

import org.modelmapper.ModelMapper;
import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.dto.ExchangeRateDTO;
import ru.dragomirov.entities.Currency;
import ru.dragomirov.entities.ExchangeRate;

public class MappingUtils {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(CurrencyDTO.class, Currency.class)
                .addMapping(CurrencyDTO::getName, Currency::setFullName);
    }

    public static Currency currencyToEntity(CurrencyDTO currencyDTO) {
        return MODEL_MAPPER.map(currencyDTO, Currency.class);
    }

    public static CurrencyDTO currencyToDTO(Currency currency){
        return MODEL_MAPPER.map(currency, CurrencyDTO.class);
    }

    public static ExchangeRate exchangeRateToEntity(ExchangeRateDTO exchangeRateDTO){
        return MODEL_MAPPER.map(exchangeRateDTO, ExchangeRate.class);
    }

    public static ExchangeRateDTO exchangeRateToDTO(ExchangeRate exchangeRate){
        return MODEL_MAPPER.map(exchangeRate, ExchangeRateDTO.class);
    }
}
