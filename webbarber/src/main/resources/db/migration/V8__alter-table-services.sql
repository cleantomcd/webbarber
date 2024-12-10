ALTER TABLE services ADD COLUMN active BOOLEAN;
UPDATE services set active = TRUE;