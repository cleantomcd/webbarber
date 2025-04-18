CREATE TABLE timeslot (
    id TEXT UNIQUE PRIMARY KEY,
    barber_id TEXT NOT NULL,
    FOREIGN KEY (barber_id) REFERENCES barbers(id),
    day_of_week SMALLINT NOT NULL,
    am_start_time TIME,
    am_end_time TIME,
    pm_start_time TIME,
    pm_end_time TIME,
    interval INT
);