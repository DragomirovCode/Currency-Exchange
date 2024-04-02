package com.example.DTO;

public class CurrencyDTO {
    private int ID;
    private String Code;
    private String FullName;
    private String Sign;

    public CurrencyDTO(String code, String fullName, String sign) {
        Code = code;
        FullName = fullName;
        Sign = sign;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }
}
