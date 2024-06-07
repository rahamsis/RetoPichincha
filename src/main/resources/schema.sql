CREATE TABLE currency_exchange (id SERIAL PRIMARY KEY, currency_from VARCHAR(255), currency_to VARCHAR(255), conversion DOUBLE);

CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255), roles VARCHAR(255));

CREATE TABLE currency_operation (id SERIAL PRIMARY KEY, user_id BIGINT, username VARCHAR(255), currency_from VARCHAR(255), currency_to VARCHAR(255),
conversion DOUBLE, amount DOUBLE, amount_converted DOUBLE);
