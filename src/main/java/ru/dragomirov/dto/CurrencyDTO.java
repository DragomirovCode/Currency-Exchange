package ru.dragomirov.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String fullName;

    @SerializedName("code")
    private String code;

    @SerializedName("sign")
    private String sign;

    public CurrencyDTO(String fullName,String code, String sign) {
        this.fullName = fullName;
        this.code = code;
        this.sign = sign;
    }
}
