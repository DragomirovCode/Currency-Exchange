CREATE TABLE ExchangeRates (
                               id INTEGER PRIMARY KEY AUTOINCREMENT,
                               base_currency_id INTEGER,
                               target_currency_id INTEGER,
                               rate DECIMAL(6),
                               FOREIGN KEY (base_currency_id) REFERENCES currency(id),
                               FOREIGN KEY (target_currency_id) REFERENCES currency(id)
);

INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES ((SELECT id FROM Currency WHERE code = 'USD'), (SELECT id FROM Currency WHERE code = 'EUR'), 0.85);
INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES ((SELECT id FROM Currency WHERE code = 'EUR'), (SELECT id FROM Currency WHERE code = 'JPY'), 130.33);
INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES ((SELECT id FROM Currency WHERE code = 'JPY'), (SELECT id FROM Currency WHERE code = 'GBP'), 0.0067);
INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES ((SELECT id FROM Currency WHERE code = 'GBP'), (SELECT id FROM Currency WHERE code = 'AUD'), 1.83);
INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES ((SELECT id FROM Currency WHERE code = 'AUD'), (SELECT id FROM Currency WHERE code = 'USD'), 0.73);
