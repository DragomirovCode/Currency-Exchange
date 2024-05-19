package ru.dragomirov.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDTO {
    private int id;
    private String name;
    private String code;
    private String sign;
}
