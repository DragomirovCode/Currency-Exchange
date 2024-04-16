CREATE TABLE ExchangeRates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    baseCurrencyId INTEGER,
    targetCurrencyId INTEGER,
    rate DECIMAL(6),
    FOREIGN KEY (baseCurrencyId) REFERENCES currency(id),
    FOREIGN KEY (targetCurrencyId) REFERENCES currency(id)
);
