package ru.dragomirov.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String fullName;

    @SerializedName("code")
    private String code;

    @SerializedName("sign")
    private String sign;

    public Currency(String fullName, String code, String sign) {
        this.fullName = fullName;
        this.code = code;
        this.sign = sign;
    }
}
