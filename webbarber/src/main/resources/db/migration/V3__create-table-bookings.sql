CREATE TABLE bookings (
    id TEXT UNIQUE PRIMARY KEY,
    barber_id TEXT NOT NULL,
    FOREIGN KEY (barber_id) REFERENCES barbers(id),
    user_id TEXT NOT NULL,
    service_id TEXT NOT NULL,
    "date" DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);