package ru.dragomirov.utils;

import org.modelmapper.ModelMapper;
import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.entities.Currency;

public class MappingUtils {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(CurrencyDTO.class, Currency.class)
                .addMapping(CurrencyDTO::getName, Currency::setFullName);
    }

    public static Currency toEntity(CurrencyDTO currencyDTO) {
        return MODEL_MAPPER.map(currencyDTO, Currency.class);
    }

    public static CurrencyDTO toDTO(Currency currency){
        return MODEL_MAPPER.map(currency, CurrencyDTO.class);
    }
}
