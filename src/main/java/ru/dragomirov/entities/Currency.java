package ru.dragomirov.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency {
    private int id;

    @SerializedName("name")
    private String fullName;

    private String code;

    private String sign;

    public Currency(String fullName, String code, String sign) {
        this.fullName = fullName;
        this.code = code;
        this.sign = sign;
    }
}
