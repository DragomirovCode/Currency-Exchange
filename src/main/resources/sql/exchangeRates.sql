CREATE TABLE ExchangeRates (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId INTEGER,
    TargetCurrencyId INTEGER,
    Rate DECIMAL(6),
    FOREIGN KEY (BaseCurrencyId) REFERENCES currency(ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES currency(ID)
);
