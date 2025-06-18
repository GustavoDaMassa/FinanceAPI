CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    parent_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    UNIQUE(name, parent_id, user_id)
);


ALTER TABLE transactions
    DROP COLUMN classification,
    DROP COLUMN specification,
    ADD COLUMN category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    ADD COLUMN subcategory_id BIGINT REFERENCES categories(id) ON DELETE SET NULL;
