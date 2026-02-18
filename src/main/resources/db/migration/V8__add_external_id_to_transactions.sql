ALTER TABLE transactions ADD COLUMN external_id VARCHAR(255);
ALTER TABLE transactions ADD CONSTRAINT uk_transactions_external_id UNIQUE (external_id);
