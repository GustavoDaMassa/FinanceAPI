ALTER TABLE accounts
ADD COLUMN pluggy_account_id VARCHAR(255);

ALTER TABLE accounts
ADD CONSTRAINT uk_pluggy_account_id UNIQUE (pluggy_account_id);
