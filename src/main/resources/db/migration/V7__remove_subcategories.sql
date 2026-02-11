-- Remove subcategory_id column from transactions
ALTER TABLE transactions DROP COLUMN IF EXISTS subcategory_id;

-- Drop existing unique constraint on categories (name, parent_id, user_id)
ALTER TABLE categories DROP CONSTRAINT IF EXISTS categories_name_parent_id_user_id_key;

-- Remove parent_id column from categories
ALTER TABLE categories DROP COLUMN IF EXISTS parent_id;

-- Add new unique constraint on categories (name, user_id)
ALTER TABLE categories ADD CONSTRAINT categories_name_user_id_key UNIQUE (name, user_id);
