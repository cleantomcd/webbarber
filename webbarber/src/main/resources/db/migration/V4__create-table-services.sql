CREATE TABLE services (
    id TEXT UNIQUE PRIMARY KEY,
    barber_id TEXT NOT NULL,
    FOREIGN KEY (barber_id) REFERENCES barbers(id),
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100),
    price_in_cents INT DEFAULT 0,
    duration INTEGER NOT NULL,
    active BOOLEAN DEFAULT TRUE
);
