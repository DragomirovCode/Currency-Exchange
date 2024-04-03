package com.example.dto;

import com.google.gson.annotations.SerializedName;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
