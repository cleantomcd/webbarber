ALTER TABLE users ADD COLUMN role TEXT;
UPDATE users SET role = 'userId';