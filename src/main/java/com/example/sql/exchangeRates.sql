CREATE TABLE ExchangeRates (
                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                               baseCurrencyId INTEGER,
                               targetCurrencyId INTEGER,
                               rate DECIMAL(6),
                               FOREIGN KEY (baseCurrencyId) REFERENCES currency(id),
                               FOREIGN KEY (targetCurrencyId) REFERENCES currency(id)
);


INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES
                                                                       (1, 2, 0.86),  -- USD to EUR
                                                                       (1, 3, 0.73),  -- USD to GBP
                                                                       (1, 4, 111.15),  -- USD to JPY
                                                                       (2, 1, 1.16),  -- EUR to USD
                                                                       (2, 3, 0.85),  -- EUR to GBP
                                                                       (2, 4, 129.58),  -- EUR to JPY
                                                                       (3, 1, 1.37),  -- GBP to USD
                                                                       (3, 2, 1.17),  -- GBP to EUR
                                                                       (3, 4, 151.89),  -- GBP to JPY
                                                                       (4, 1, 0.0090),  -- JPY to USD
                                                                       (4, 2, 0.0077),  -- JPY to EUR
                                                                       (4, 3, 0.0066);  -- JPY to GBP
