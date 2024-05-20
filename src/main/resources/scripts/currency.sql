CREATE TABLE Currency (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          code VARCHAR(3) UNIQUE,
                          full_name VARCHAR(100) UNIQUE,
                          sign VARCHAR(5)
);

INSERT INTO Currency (code, full_name, sign) VALUES ('USD', 'United States Dollar', '$');
INSERT INTO Currency (code, full_name, sign) VALUES ('EUR', 'Euro', '€');
INSERT INTO Currency (code, full_name, sign) VALUES ('JPY', 'Japanese Yen', '¥');
INSERT INTO Currency (code, full_name, sign) VALUES ('GBP', 'British Pound', '£');
INSERT INTO Currency (code, full_name, sign) VALUES ('AUD', 'Australian Dollar', 'A$');
