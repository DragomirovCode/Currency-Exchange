CREATE TABLE Currency (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          code VARCHAR,
                          fullName VARCHAR,
                          sign VARCHAR
);



INSERT INTO Currency (code, fullName, sign) VALUES
                                                ('USD', 'United States Dollar', '$'),
                                                ('EUR', 'Euro', '€'),
                                                ('GBP', 'British Pound Sterling', '£'),
                                                ('JPY', 'Japanese Yen', '¥');

