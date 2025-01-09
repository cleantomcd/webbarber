CREATE TABLE timeslot_override (
    id TEXT UNIQUE PRIMARY KEY,
    "date" DATE NOT NULL,
    am_start_time TIME,
    am_end_time TIME,
    pm_start_time TIME,
    pm_end_time TIME,
    interval INT,
    is_closed BOOLEAN,
    closed_slots TEXT[]
);