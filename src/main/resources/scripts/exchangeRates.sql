INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES ((SELECT ID FROM Currency WHERE Code = 'USD'), (SELECT ID FROM Currency WHERE Code = 'EUR'), 0.85);
INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES ((SELECT ID FROM Currency WHERE Code = 'EUR'), (SELECT ID FROM Currency WHERE Code = 'JPY'), 130.33);
INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES ((SELECT ID FROM Currency WHERE Code = 'JPY'), (SELECT ID FROM Currency WHERE Code = 'GBP'), 0.0067);
INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES ((SELECT ID FROM Currency WHERE Code = 'GBP'), (SELECT ID FROM Currency WHERE Code = 'AUD'), 1.83);
INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES ((SELECT ID FROM Currency WHERE Code = 'AUD'), (SELECT ID FROM Currency WHERE Code = 'USD'), 0.73);
