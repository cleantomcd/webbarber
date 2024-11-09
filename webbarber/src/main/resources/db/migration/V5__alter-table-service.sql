ALTER TABLE service ADD COLUMN price_in_cents INT;
UPDATE service SET price_in_cents = 0;