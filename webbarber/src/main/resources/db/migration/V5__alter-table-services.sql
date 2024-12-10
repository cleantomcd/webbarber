ALTER TABLE services ADD COLUMN price_in_cents INT;
UPDATE services SET price_in_cents = 0;