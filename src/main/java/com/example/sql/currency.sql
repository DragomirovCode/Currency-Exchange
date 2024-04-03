CREATE TABLE Currency (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          fullName VARCHAR,
                          code VARCHAR,
                          sign VARCHAR
);



INSERT INTO Currency (fullName,code, sign) VALUES
                                                ('United States Dollar', 'USD', '$'),
                                                ('Euro', 'EUR', '€'),
                                                ('British Pound Sterling', 'GBP', '£'),
                                                ('Japanese Yen','JPY', '¥');

