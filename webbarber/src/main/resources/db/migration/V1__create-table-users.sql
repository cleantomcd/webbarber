CREATE TABLE users (
    id TEXT UNIQUE PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    amount_booked_services INT DEFAULT 0,
    role TEXT DEFAULT 'admin'
);
