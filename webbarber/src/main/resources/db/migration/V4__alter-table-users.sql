ALTER TABLE users ADD COLUMN amount_booked_services INT;
UPDATE users SET amount_booked_services = 0;
