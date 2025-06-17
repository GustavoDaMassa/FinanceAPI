
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE financial_integrations(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    aggregator VARCHAR(50) NOT NULL,
    link_id VARCHAR(255) UNIQUE NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT now(),
    expires_at TIMESTAMP
);

CREATE TABLE accounts(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    integration_id BIGINT REFERENCES financial_integrations(id) ON DELETE SET NULL,
    institution VARCHAR(100),
    type VARCHAR(50),
    account_number VARCHAR(100),
    balance NUMERIC(15, 2)
);

CREATE TABLE transactions(
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    amount NUMERIC(15,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    description TEXT,
    source VARCHAR(255),
    destination VARCHAR(255),
    transaction_date DATE,
    classification VARCHAR(100),
    specification VARCHAR(100)
);