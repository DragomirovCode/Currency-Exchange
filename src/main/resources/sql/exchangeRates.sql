CREATE TABLE ExchangeRates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER,
    target_currency_id INTEGER,
    rate DECIMAL(6),
    FOREIGN KEY (base_currency_id) REFERENCES currency(id),
    FOREIGN KEY (target_currency_id) REFERENCES currency(id)
);
